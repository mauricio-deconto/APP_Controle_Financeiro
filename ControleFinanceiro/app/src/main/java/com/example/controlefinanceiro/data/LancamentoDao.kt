package com.example.controlefinanceiro.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.controlefinanceiro.model.Lancamento

/**
 * DAO = Data Access Object.
 * É uma interface onde declaramos QUAIS operações queremos fazer no banco.
 * O Room gera automaticamente (em tempo de compilação) o código real do SQLite
 * para cada uma dessas funções — não precisamos escrever SQL manual, exceto
 * dentro da anotação @Query.
 */
@Dao
interface LancamentoDao {

    /**
     * Insere um novo lançamento no banco.
     * "suspend" indica que essa função deve ser chamada dentro de uma coroutine,
     * ou seja, ela roda em segundo plano e não trava a tela (UI) enquanto salva.
     */
    @Insert
    suspend fun inserir(lancamento: Lancamento)

    /**
     * Retorna TODOS os lançamentos, ordenados do mais recente (maior id) para o mais antigo.
     * Usamos LiveData: isso significa que, sempre que a tabela mudar (um novo lançamento
     * for inserido), a lista na tela é atualizada AUTOMATICAMENTE, sem precisarmos
     * recarregar a tela manualmente.
     */
    @Query("SELECT * FROM lancamentos ORDER BY id DESC")
    fun listarTodos(): LiveData<List<Lancamento>>
}
