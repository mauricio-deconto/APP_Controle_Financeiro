package com.example.controlefinanceiro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.controlefinanceiro.data.LancamentoRepository

/**
 * O Android, por padrão, só sabe criar ViewModels com construtor vazio.
 * Como nosso LancamentoViewModel precisa receber um "repository" no construtor,
 * precisamos de uma Factory (fábrica) que ensina o Android a criar essa instância.
 */
class LancamentoViewModelFactory(private val repository: LancamentoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LancamentoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LancamentoViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
