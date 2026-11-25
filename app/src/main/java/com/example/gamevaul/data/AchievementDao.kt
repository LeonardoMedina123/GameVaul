
package com.example.gamevaul.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    // Obtener los logros de un videojuego
    @Query("SELECT * FROM achievements WHERE gameId = :gameId ORDER BY id ASC")
    fun getAchievementsByGame(gameId: Int): Flow<List<AchievementEntity>>

    // Insertar un logro
    @Insert
    suspend fun insertAchievement(achievement: AchievementEntity)

    // Actualizar un logro
    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    // Eliminar un logro
    @Delete
    suspend fun deleteAchievement(achievement: AchievementEntity)

    // Eliminar varios logros seleccionados
    @Delete
    suspend fun deleteAchievements(achievements: List<AchievementEntity>)

    // Contar los logros conseguidos
    @Query("SELECT COUNT(*) FROM achievements WHERE gameId = :gameId AND isCompleted = 1")
    fun getCompletedCount(gameId: Int): Flow<Int>

    // Contar todos los logros de un videojuego
    @Query("SELECT COUNT(*) FROM achievements WHERE gameId = :gameId")
    fun getTotalCount(gameId: Int): Flow<Int>
}
