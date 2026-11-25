
package com.example.gamevaul.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: GameDatabase? = null

    fun getDatabase(context: Context): GameDatabase {

        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                GameDatabase::class.java,
                "gamevault_database"
            )
                .addMigrations(GameDatabase.MIGRATION_1_2)
                .build()

            INSTANCE = instance

            instance
        }
    }
}
