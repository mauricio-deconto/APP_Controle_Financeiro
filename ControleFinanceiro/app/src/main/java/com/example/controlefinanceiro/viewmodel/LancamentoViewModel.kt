package com.example.controlefinanceiro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controlefinanceiro.data.LancamentoRepository
import com.example.controlefinanceiro.model.Lancamento
import kotlinx.coroutines.launch

/**
 * VIEWMODEL (camada VM do MVVM).
 *
 * O ViewModel é o "cérebro" da tela: ele NÃO conhece nada de XML/Views,
 * apenas expõe dados (LiveData) e funções que a Activity pode chamar.
 *
 * Vantagem: o ViewModel sobrevive a mudanças de configuração (ex: girar a tela),
 * então os dados não se perdem, e a Activity fica bem mais "magra" (só cuida da UI).
 */
class LancamentoViewModel(private val repository: LancamentoRepository) : ViewModel() {

    // Lista de lançamentos exposta para a tela de Extrato observar.
    val listaLancamentos: LiveData<List<Lancamento>> = repository.listaLancamentos

    /**
     * Salva um novo lançamento.
     * Usamos "viewModelScope.launch" para rodar a inserção no banco em uma coroutine,
     * fora da thread principal (thread de UI) — assim o app não trava enquanto salva.
     */
    fun salvarLancamento(valor: Double, descricao: String, data: String, tipo: com.example.controlefinanceiro.model.TipoLancamento) {
        viewModelScope.launch {
            val novoLancamento = Lancamento(
                valor = valor,
                descricao = descricao,
                data = data,
                tipo = tipo
            )
            repository.inserir(novoLancamento)
        }
    }
}
