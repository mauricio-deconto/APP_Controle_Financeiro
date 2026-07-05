package com.example.controlefinanceiro

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.controlefinanceiro.data.AppDatabase
import com.example.controlefinanceiro.data.LancamentoRepository
import com.example.controlefinanceiro.databinding.ActivityMainBinding
import com.example.controlefinanceiro.model.TipoLancamento
import com.example.controlefinanceiro.viewmodel.LancamentoViewModel
import com.example.controlefinanceiro.viewmodel.LancamentoViewModelFactory
import java.util.Calendar
import java.util.Locale

/**
 * VIEW (camada V do MVVM).
 *
 * Esta é a Tela de Lançamento (Cadastro): tela principal (launcher) do app.
 * Responsabilidades da Activity nesse padrão MVVM:
 *  - Desenhar a tela (infla o layout via ViewBinding)
 *  - Capturar interações do usuário (cliques, digitação)
 *  - Repassar essas ações para o ViewModel (que decide o que fazer com os dados)
 * A Activity NÃO acessa o banco de dados diretamente — isso é papel do
 * Repository/DAO, chamados através do ViewModel.
 */
class MainActivity : AppCompatActivity() {

    // ViewBinding: gera automaticamente uma classe com referência a cada view do XML,
    // evitando a necessidade de findViewById() e evitando erros de "view nula".
    private lateinit var binding: ActivityMainBinding

    // ViewModel desta tela, responsável por salvar o lançamento.
    private lateinit var viewModel: LancamentoViewModel

    // Guarda a data selecionada no formato dd/MM/yyyy
    private var dataSelecionada: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla o layout activity_main.xml usando ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarViewModel()
        configurarCampoData()
        configurarBotoes()
    }

    /**
     * Monta a "corrente" Database -> Repository -> ViewModel (via Factory).
     * É aqui que "montamos" manualmente a injeção de dependências do app
     * (sem usar bibliotecas como o Hilt, para manter o projeto simples).
     */
    private fun configurarViewModel() {
        val dao = AppDatabase.getDatabase(applicationContext).lancamentoDao()
        val repository = LancamentoRepository(dao)
        val factory = LancamentoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LancamentoViewModel::class.java]
    }

    /**
     * Configura o campo de Data para abrir um DatePickerDialog ao ser tocado
     * (item bônus do checklist: uso de DatePicker), em vez de digitação livre.
     */
    private fun configurarCampoData() {
        binding.edtData.setOnClickListener {
            val calendario = Calendar.getInstance()
            val ano = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, anoEscolhido, mesEscolhido, diaEscolhido ->
                    // O mês do DatePicker começa em 0 (Janeiro = 0), por isso o +1
                    dataSelecionada = String.format(
                        Locale("pt", "BR"),
                        "%02d/%02d/%04d",
                        diaEscolhido, mesEscolhido + 1, anoEscolhido
                    )
                    binding.edtData.setText(dataSelecionada)
                },
                ano, mes, dia
            )
            datePicker.show()
        }
    }

    private fun configurarBotoes() {
        // Botão SALVAR: valida os campos e persiste o lançamento
        binding.btnSalvar.setOnClickListener {
            salvarLancamento()
        }

        // Botão para ir até a Tela de Extrato
        binding.btnVerExtrato.setOnClickListener {
            startActivity(Intent(this, ExtratoActivity::class.java))
        }
    }

    /**
     * Valida os campos da tela e, se estiverem corretos, manda o ViewModel
     * salvar o lançamento no banco de dados.
     */
    private fun salvarLancamento() {
        // Limpa erros anteriores dos campos
        binding.layoutValor.error = null
        binding.layoutDescricao.error = null
        binding.layoutData.error = null

        val textoValor = binding.edtValor.text.toString().trim()
        val descricao = binding.edtDescricao.text.toString().trim()

        // --- Validação do Valor ---
        val valor = textoValor.toDoubleOrNull()
        if (textoValor.isEmpty() || valor == null || valor <= 0.0) {
            binding.layoutValor.error = getString(R.string.erro_valor_vazio)
            return
        }

        // --- Validação da Descrição ---
        if (descricao.isEmpty()) {
            binding.layoutDescricao.error = getString(R.string.erro_descricao_vazia)
            return
        }

        // --- Validação da Data ---
        if (dataSelecionada.isEmpty()) {
            binding.layoutData.error = getString(R.string.erro_data_vazia)
            return
        }

        // --- Verifica o tipo selecionado no RadioGroup ---
        val tipo = if (binding.radioReceita.isChecked) {
            TipoLancamento.RECEITA
        } else {
            TipoLancamento.DESPESA
        }

        // Pede ao ViewModel para persistir o lançamento no banco (Room/SQLite)
        viewModel.salvarLancamento(valor, descricao, dataSelecionada, tipo)

        Toast.makeText(this, R.string.msg_salvo_sucesso, Toast.LENGTH_SHORT).show()
        limparCampos()
    }

    /** Limpa os campos da tela após salvar, preparando para um novo lançamento. */
    private fun limparCampos() {
        binding.edtValor.text?.clear()
        binding.edtDescricao.text?.clear()
        binding.edtData.text?.clear()
        binding.radioReceita.isChecked = true
        dataSelecionada = ""
    }
}
