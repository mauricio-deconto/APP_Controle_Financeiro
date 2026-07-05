package com.example.controlefinanceiro.view

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.controlefinanceiro.R
import com.example.controlefinanceiro.databinding.ItemLancamentoBinding
import com.example.controlefinanceiro.model.Lancamento
import com.example.controlefinanceiro.model.TipoLancamento
import java.text.NumberFormat
import java.util.Locale

/**
 * ADAPTER: é a "ponte" entre a LISTA DE DADOS (List<Lancamento>) e a RecyclerView.
 * A RecyclerView não sabe desenhar objetos Lancamento sozinha; o Adapter é quem
 * diz "para o item da posição X, desenhe esses dados neste layout (item_lancamento.xml)".
 *
 * Usamos uma lista mutável interna (dados) e o método atualizarLista() para
 * receber novos dados vindos do LiveData observado na Activity.
 */
class LancamentoAdapter : RecyclerView.Adapter<LancamentoAdapter.LancamentoViewHolder>() {

    // Lista local que o Adapter usa para desenhar os itens.
    private var dados: List<Lancamento> = emptyList()

    /**
     * Chamado pela Activity sempre que a lista de lançamentos mudar no banco.
     * notifyDataSetChanged() avisa a RecyclerView para redesenhar a lista.
     */
    fun atualizarLista(novaLista: List<Lancamento>) {
        dados = novaLista
        notifyDataSetChanged()
    }

    /**
     * ViewHolder: guarda a referência das views de UM item da lista (via ViewBinding),
     * evitando ficar chamando findViewById toda hora (mais performático).
     */
    inner class LancamentoViewHolder(val binding: ItemLancamentoBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Chamado quando a RecyclerView precisa criar um novo "slot" visual (infla o XML).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LancamentoViewHolder {
        val binding = ItemLancamentoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LancamentoViewHolder(binding)
    }

    // Retorna quantos itens existem na lista.
    override fun getItemCount(): Int = dados.size

    // Chamado para preencher os dados de UM item específico (posição "position") na tela.
    override fun onBindViewHolder(holder: LancamentoViewHolder, position: Int) {
        val lancamento = dados[position]
        val context = holder.itemView.context

        // Formata o valor no padrão monetário brasileiro (R$ 1.234,56)
        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val valorFormatado = formatoMoeda.format(lancamento.valor)

        holder.binding.txtDescricao.text = lancamento.descricao
        holder.binding.txtData.text = lancamento.data

        // Diferenciação visual entre Receita (verde) e Despesa (vermelho) - item bônus
        if (lancamento.tipo == TipoLancamento.RECEITA) {
            holder.binding.txtValor.text = "+ $valorFormatado"
            holder.binding.txtValor.setTextColor(ContextCompat.getColor(context, R.color.receita_verde))
            holder.binding.imgIconeTipo.setImageResource(R.drawable.ic_income)
            pintarFundoIcone(holder, R.color.receita_verde)
        } else {
            holder.binding.txtValor.text = "- $valorFormatado"
            holder.binding.txtValor.setTextColor(ContextCompat.getColor(context, R.color.despesa_vermelho))
            holder.binding.imgIconeTipo.setImageResource(R.drawable.ic_expense)
            pintarFundoIcone(holder, R.color.despesa_vermelho)
        }
    }

    /**
     * Pinta o círculo de fundo do ícone com a cor correspondente ao tipo
     * (verde para receita, vermelho para despesa).
     */
    private fun pintarFundoIcone(holder: LancamentoViewHolder, corResId: Int) {
        val context = holder.itemView.context
        val background = holder.binding.imgIconeTipo.background.mutate() as GradientDrawable
        background.setColor(ContextCompat.getColor(context, corResId))
    }
}
