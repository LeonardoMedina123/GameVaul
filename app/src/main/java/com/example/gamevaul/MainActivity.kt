
package com.example.gamevaul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamevaul.data.GameEntity
import com.example.gamevaul.ui.AchievementScreen
import com.example.gamevaul.ui.GameListScreen
import com.example.gamevaul.ui.theme.GameVaulTheme
import com.example.gamevaul.viewmodel.AchievementViewModel
import com.example.gamevaul.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GameVaulTheme {
                val gameViewModel: GameViewModel = viewModel()
                val achievementViewModel: AchievementViewModel = viewModel()

                var selectedGame by remember {
                    mutableStateOf<GameEntity?>(null)
                }

                if (selectedGame == null) {
                    GameListScreen(
                        viewModel = gameViewModel,
                        onGameClick = { game ->
                            selectedGame = game
                        }
                    )
                } else {
                    AchievementScreen(
                        game = selectedGame!!,
                        viewModel = achievementViewModel,
                        onBack = {
                            selectedGame = null
                        }
                    )
                }
            }
        }
    }
}
