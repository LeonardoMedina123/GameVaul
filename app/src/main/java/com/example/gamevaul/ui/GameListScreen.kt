
package com.example.gamevaul.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gamevaul.R
import com.example.gamevaul.data.GameEntity
import com.example.gamevaul.data.GameWithProgress
import com.example.gamevaul.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(
    viewModel: GameViewModel,
    onGameClick: (GameEntity) -> Unit
) {
    val games by viewModel.games.collectAsState()

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var gameToEdit by remember {
        mutableStateOf<GameEntity?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("GameVault - Biblioteca")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                }
            ) {
                Text("+")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (games.isEmpty()) {

                Text(
                    text = "No hay videojuegos en tu biblioteca. ¡Agrega uno!",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(24.dp)
                )

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = games,
                        key = { game -> game.id }
                    ) { game ->

                        GameItem(
                            game = game,

                            onClick = {
                                onGameClick(game.toGameEntity())
                            },

                            onEdit = {
                                gameToEdit = game.toGameEntity()
                            },

                            onDelete = {
                                viewModel.deleteGame(game.toGameEntity())
                            }
                        )
                    }
                }
            }
        }

        // Diálogo para agregar un videojuego
        if (showAddDialog) {

            AddGameDialog(
                onDismiss = {
                    showAddDialog = false
                },
                onConfirm = { title, platform, category ->

                    viewModel.insertGame(
                        title = title,
                        platform = platform,
                        category = category
                    )

                    showAddDialog = false
                }
            )
        }

        // Diálogo para editar un videojuego
        gameToEdit?.let { game ->

            AddGameDialog(
                game = game,
                onDismiss = {
                    gameToEdit = null
                },
                onConfirm = { title, platform, category ->

                    viewModel.updateGame(
                        game.copy(
                            title = title,
                            platform = platform
                        ),
                        category
                    )

                    gameToEdit = null
                }
            )
        }
    }
}

@Composable
fun GameItem(
    game: GameWithProgress,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current

    // Buscar la imagen correspondiente al videojuego
    val imageId = remember(game.imageResName) {
        context.resources.getIdentifier(
            game.imageResName,
            "drawable",
            context.packageName
        )
    }

    // Si no encuentra la imagen, utilizar sandbox
    val finalImageId = if (imageId != 0) {
        imageId
    } else {
        R.drawable.sandbox
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Portada del videojuego
            Image(
                painter = painterResource(id = finalImageId),
                contentDescription = "Portada de ${game.title}",
                modifier = Modifier.size(
                    width = 100.dp,
                    height = 130.dp
                ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = game.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${game.platform} • ${game.status}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Progreso automático de logros
                LinearProgressIndicator(
                    progress = {
                        (game.progress / 100f).coerceIn(0f, 1f)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Progreso: ${game.progress}%",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        onClick = onEdit
                    ) {
                        Text("Editar")
                    }

                    TextButton(
                        onClick = onDelete
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

// Convertir los datos de progreso a la entidad del videojuego
private fun GameWithProgress.toGameEntity(): GameEntity {
    return GameEntity(
        id = id,
        title = title,
        platform = platform,
        imageResName = imageResName
    )
}
