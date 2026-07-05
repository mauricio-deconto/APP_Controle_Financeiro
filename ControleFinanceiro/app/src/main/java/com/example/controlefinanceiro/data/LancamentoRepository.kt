package com.example.controlefinanceiro.data

import androidx.lifecycle.LiveData
import com.example.controlefinanceiro.model.Lancamento

/**
 * REPOSITORY: camada intermediária entre o ViewModel e a fonte de dados (o DAO/Room).
 *
 * Por que ter essa camada extra, já que ela "só repassa" as chamadas?
 * Porque, no futuro, se você quiser trocar o SQLite por outra fonte
 * (ex: Firebase, uma API remota), só precisa mexer AQUI dentro.
 * O ViewModel e as Activities nem ficam sabendo da mudança.
 */
class LancamentoRepository(private val dao: LancamentoDao) {

    // Lista de lançamentos, já pronta para ser observada pela tela (LiveData).
    val listaLancamentos: LiveData<List<Lancamento>> = dao.listarTodos()

    // Função de inserção; apenas repassa a chamada para o DAO.
    suspend fun inserir(lancamento: Lancamento) {
        dao.inserir(lancamento)
    }
}
