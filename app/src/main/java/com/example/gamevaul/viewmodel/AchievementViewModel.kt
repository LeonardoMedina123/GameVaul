
package com.example.gamevaul.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevaul.data.AchievementEntity
import com.example.gamevaul.data.DatabaseProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AchievementViewModel(application: Application) : AndroidViewModel(application) {

    private val achievementDao =
        DatabaseProvider.getDatabase(application).achievementDao()

    // Obtener los logros de un videojuego
    fun getAchievementsByGame(gameId: Int): Flow<List<AchievementEntity>> {
        return achievementDao.getAchievementsByGame(gameId)
    }

    // Insertar un logro
    fun insertAchievement(
        gameId: Int,
        name: String,
        description: String,
        isCompleted: Boolean
    ) {
        viewModelScope.launch {
            achievementDao.insertAchievement(
                AchievementEntity(
                    gameId = gameId,
                    name = name,
                    description = description,
                    isCompleted = isCompleted
                )
            )
        }
    }

    // Actualizar un logro
    fun updateAchievement(achievement: AchievementEntity) {
        viewModelScope.launch {
            achievementDao.updateAchievement(achievement)
        }
    }

    // Eliminar un logro
    fun deleteAchievement(achievement: AchievementEntity) {
        viewModelScope.launch {
            achievementDao.deleteAchievement(achievement)
        }
    }

    // Eliminar varios logros
    fun deleteAchievements(achievements: List<AchievementEntity>) {
        viewModelScope.launch {
            achievementDao.deleteAchievements(achievements)
        }
    }

    // Obtener logros completados
    fun getCompletedCount(gameId: Int): Flow<Int> {
        return achievementDao.getCompletedCount(gameId)
    }

    // Obtener total de logros
    fun getTotalCount(gameId: Int): Flow<Int> {
        return achievementDao.getTotalCount(gameId)
    }
}
