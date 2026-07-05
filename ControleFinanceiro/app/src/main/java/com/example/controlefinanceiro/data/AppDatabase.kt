package com.example.controlefinanceiro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.controlefinanceiro.model.Lancamento

/**
 * Classe principal do banco de dados local (SQLite, gerenciado pelo Room).
 *
 * @Database diz ao Room: "essa classe representa o banco, e as tabelas
 * (entities) que ele deve criar são as listadas em 'entities'".
 */
@Database(entities = [Lancamento::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Registra o conversor de enum criado acima
abstract class AppDatabase : RoomDatabase() {

    // O Room implementa automaticamente essa função, retornando o DAO pronto para uso.
    abstract fun lancamentoDao(): LancamentoDao

    companion object {
        // @Volatile garante que a variável "INSTANCE" seja sempre lida/escrita
        // de forma consistente entre diferentes threads (evita bugs de concorrência).
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Padrão SINGLETON: garante que exista apenas UMA instância do banco
         * em todo o aplicativo, evitando abrir várias conexões desnecessárias.
         */
        fun getDatabase(context: Context): AppDatabase {
            // Se já existe uma instância, retorna ela. Senão, cria uma nova.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "controle_financeiro_db" // nome do arquivo do banco no dispositivo
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
