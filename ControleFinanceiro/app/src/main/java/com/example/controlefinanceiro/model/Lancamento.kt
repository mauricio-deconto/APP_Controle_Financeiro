package com.example.controlefinanceiro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * MODEL (camada M do MVVM/MVC).
 *
 * Esta classe representa UM lançamento financeiro (uma linha da tabela no banco).
 * A anotação @Entity transforma essa classe Kotlin em uma tabela do Room (SQLite).
 *
 * Cada instância dessa classe é um registro: um valor, uma descrição, uma data e um tipo.
 */
@Entity(tableName = "lancamentos")
data class Lancamento(

    // Chave primária, gerada automaticamente pelo Room (auto incremento).
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Valor monetário do lançamento (sempre positivo; o sinal é definido pelo "tipo").
    val valor: Double,

    // Descrição curta, ex: "Aluguel", "Salário".
    val descricao: String,

    // Data digitada/selecionada pelo usuário, guardada como texto no formato dd/MM/yyyy.
    val data: String,

    // Tipo do lançamento: RECEITA (entrada) ou DESPESA (saída).
    val tipo: TipoLancamento
)
