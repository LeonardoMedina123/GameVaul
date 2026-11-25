
package com.example.gamevaul.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        GameEntity::class,
        AchievementEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    abstract fun achievementDao(): AchievementDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    """
                    ALTER TABLE games
                    ADD COLUMN imageResName TEXT NOT NULL
                    DEFAULT 'game_default'
                    """.trimIndent()
                )
            }
        }
    }
}
