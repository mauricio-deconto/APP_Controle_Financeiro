package com.example.controlefinanceiro.model

/**
 * Enum simples para representar o TIPO de lançamento financeiro.
 * Usar um enum (em vez de um texto solto ou boolean) deixa o código
 * mais legível e evita erros de digitação ("Receita" vs "receita" vs "RECEITA").
 */
enum class TipoLancamento {
    RECEITA, // Dinheiro que ENTRA (crédito)
    DESPESA  // Dinheiro que SAI (débito)
}
