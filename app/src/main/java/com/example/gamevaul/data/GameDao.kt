
package com.example.gamevaul.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    // Obtener todos los videojuegos
    @Query("SELECT * FROM games ORDER BY title ASC")
    fun getAllGames(): Flow<List<GameEntity>>

    // Obtener videojuegos con su progreso y estado automáticos
    @Query("""
        SELECT
            games.id AS id,
            games.title AS title,
            games.platform AS platform,
            games.imageResName AS imageResName,
            COUNT(achievements.id) AS totalCount,
            COALESCE(
                SUM(
                    CASE
                        WHEN achievements.isCompleted = 1 THEN 1
                        ELSE 0
                    END
                ), 0
            ) AS completedCount
        FROM games
        LEFT JOIN achievements
            ON games.id = achievements.gameId
        GROUP BY games.id
        ORDER BY games.title ASC
    """)
    fun getAllGamesWithProgress(): Flow<List<GameWithProgress>>

    // Obtener un videojuego por su ID
    @Query("SELECT * FROM games WHERE id = :gameId")
    fun getGameById(gameId: Int): Flow<GameEntity?>

    // Insertar un videojuego
    @Insert
    suspend fun insertGame(game: GameEntity)

    // Actualizar un videojuego
    @Update
    suspend fun updateGame(game: GameEntity)

    // Eliminar un videojuego
    @Delete
    suspend fun deleteGame(game: GameEntity)

    // Eliminar varios videojuegos seleccionados
    @Delete
    suspend fun deleteGames(games: List<GameEntity>)
}
