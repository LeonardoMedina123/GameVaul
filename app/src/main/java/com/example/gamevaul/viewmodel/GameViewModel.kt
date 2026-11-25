
package com.example.gamevaul.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevaul.data.DatabaseProvider
import com.example.gamevaul.data.GameEntity
import com.example.gamevaul.data.GameWithProgress
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val gameDao = DatabaseProvider
        .getDatabase(application)
        .gameDao()

    // Lista de juegos con su progreso y estado calculados automáticamente
    val games: StateFlow<List<GameWithProgress>> =
        gameDao.getAllGamesWithProgress().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Convertir categoría en nombre de imagen
    private fun getImageFromCategory(category: String): String {
        return when (category) {
            "Plataformas" -> "platformer"
            "Sandbox" -> "sandbox"
            "Carreras" -> "carreras"
            "Deportes" -> "deportes"
            "Disparos" -> "disparos"
            else -> "sandbox"
        }
    }

    // Agregar un videojuego
    fun insertGame(
        title: String,
        platform: String,
        category: String
    ) {
        viewModelScope.launch {
            gameDao.insertGame(
                GameEntity(
                    title = title,
                    platform = platform,
                    description = "",
                    status = "Pendiente",
                    progress = 0,
                    imageResName = getImageFromCategory(category)
                )
            )
        }
    }

    // Actualizar un videojuego conservando la categoría seleccionada
    fun updateGame(
        game: GameEntity,
        category: String
    ) {
        viewModelScope.launch {
            gameDao.updateGame(
                game.copy(
                    imageResName = getImageFromCategory(category)
                )
            )
        }
    }

    // Actualizar un videojuego conservando su imagen actual
    fun updateGame(game: GameEntity) {
        viewModelScope.launch {
            gameDao.updateGame(game)
        }
    }

    // Eliminar un videojuego
    fun deleteGame(game: GameEntity) {
        viewModelScope.launch {
            gameDao.deleteGame(game)
        }
    }

    // Eliminar varios videojuegos
    fun deleteGames(games: List<GameEntity>) {
        viewModelScope.launch {
            gameDao.deleteGames(games)
        }
    }
}
