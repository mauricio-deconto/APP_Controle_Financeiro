package com.example.controlefinanceiro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlefinanceiro.data.AppDatabase
import com.example.controlefinanceiro.data.LancamentoRepository
import com.example.controlefinanceiro.databinding.ActivityExtratoBinding
import com.example.controlefinanceiro.model.Lancamento
import com.example.controlefinanceiro.model.TipoLancamento
import com.example.controlefinanceiro.view.LancamentoAdapter
import com.example.controlefinanceiro.viewmodel.LancamentoViewModel
import com.example.controlefinanceiro.viewmodel.LancamentoViewModelFactory
import java.text.NumberFormat
import java.util.Locale

/**
 * VIEW (camada V do MVVM).
 *
 * Tela de Extrato (Fluxo de Caixa): exibe a lista de todos os lançamentos
 * cadastrados e o saldo total (soma das receitas - soma das despesas).
 */
class ExtratoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExtratoBinding
    private lateinit var viewModel: LancamentoViewModel

    // Adapter responsável por desenhar cada item da lista na RecyclerView
    private val adapter = LancamentoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExtratoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarViewModel()
        configurarRecyclerView()
        observarLancamentos()
    }

    private fun configurarViewModel() {
        val dao = AppDatabase.getDatabase(applicationContext).lancamentoDao()
        val repository = LancamentoRepository(dao)
        val factory = LancamentoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LancamentoViewModel::class.java]
    }

    /**
     * Configura a RecyclerView: define o layout (lista vertical) e o adapter
     * que vai desenhar cada item (linha) da lista.
     */
    private fun configurarRecyclerView() {
        binding.recyclerViewLancamentos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLancamentos.adapter = adapter
    }

    /**
     * Observa o LiveData exposto pelo ViewModel. Sempre que a lista de
     * lançamentos mudar no banco (um novo item inserido), este bloco
     * é executado automaticamente, atualizando a lista e o saldo na tela.
     */
    private fun observarLancamentos() {
        viewModel.listaLancamentos.observe(this) { lista ->
            adapter.atualizarLista(lista)
            atualizarSaldo(lista)
            atualizarVisibilidadeListaVazia(lista)
        }
    }

    /** Mostra uma mensagem quando ainda não há nenhum lançamento cadastrado. */
    private fun atualizarVisibilidadeListaVazia(lista: List<Lancamento>) {
        binding.txtListaVazia.visibility = if (lista.isEmpty()) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }

    /**
     * Calcula o saldo total: soma de todas as receitas menos a soma de todas as despesas.
     * Item bônus pedido no enunciado.
     */
    private fun atualizarSaldo(lista: List<Lancamento>) {
        var saldo = 0.0
        for (lancamento in lista) {
            saldo += if (lancamento.tipo == TipoLancamento.RECEITA) {
                lancamento.valor
            } else {
                -lancamento.valor
            }
        }

        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        binding.txtSaldo.text = formatoMoeda.format(saldo)
    }
}
