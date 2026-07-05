package com.example.controlefinanceiro.data

import androidx.room.TypeConverter
import com.example.controlefinanceiro.model.TipoLancamento

/**
 * O SQLite não sabe salvar um "enum" diretamente, apenas tipos simples
 * (texto, número, etc). Esta classe ensina o Room a converter:
 *   TipoLancamento -> String (para salvar no banco)
 *   String -> TipoLancamento (para ler do banco)
 */
class Converters {

    @TypeConverter
    fun fromTipoLancamento(tipo: TipoLancamento): String {
        return tipo.name
    }

    @TypeConverter
    fun toTipoLancamento(valor: String): TipoLancamento {
        return TipoLancamento.valueOf(valor)
    }
}
