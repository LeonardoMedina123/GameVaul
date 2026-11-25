
package com.example.gamevaul.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.gamevaul.data.GameEntity
import com.example.gamevaul.viewmodel.AchievementViewModel

@Composable
fun AchievementScreen(
    game: GameEntity,
    viewModel: AchievementViewModel,
    onBack: () -> Unit
) {
    val achievements by viewModel
        .getAchievementsByGame(game.id)
        .collectAsState(initial = emptyList())

    val completed by viewModel
        .getCompletedCount(game.id)
        .collectAsState(initial = 0)

    val total by viewModel
        .getTotalCount(game.id)
        .collectAsState(initial = 0)

    var showDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            TextButton(
                onClick = onBack
            ) {
                Text("← Volver")
            }

            Text(
                text = game.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = game.platform,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Logros: $completed / $total",
                style = MaterialTheme.typography.titleMedium
            )

            LinearProgressIndicator(
                progress = {
                    if (total > 0) {
                        completed.toFloat() / total
                    } else {
                        0f
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (achievements.isEmpty()) {

                Text("Todavía no hay logros registrados.")

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = achievements,
                        key = { it.id }
                    ) { achievement ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Checkbox(
                                        checked = achievement.isCompleted,

                                        onCheckedChange = { checked ->
                                            viewModel.updateAchievement(
                                                achievement.copy(
                                                    isCompleted = checked
                                                )
                                            )
                                        },

                                        enabled = true
                                    )

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = achievement.name,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = achievement.description,
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        Text(
                                            text = if (achievement.isCompleted) {
                                                "Obtenido"
                                            } else {
                                                "Pendiente"
                                            },
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {

                                    TextButton(
                                        onClick = {
                                            viewModel.deleteAchievement(achievement)
                                        }
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para agregar logros
    if (showDialog) {

        AddAchievementDialog(
            onDismiss = {
                showDialog = false
            },

            onConfirm = { name, description ->

                viewModel.insertAchievement(
                    gameId = game.id,
                    name = name,
                    description = description,
                    isCompleted = false
                )

                showDialog = false
            }
        )
    }
}

@Composable
fun AddAchievementDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Nuevo logro")
        },

        text = {
            Column {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del logro") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") }
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && description.isNotBlank()) {
                        onConfirm(name.trim(), description.trim())
                    }
                },
                enabled = name.isNotBlank() && description.isNotBlank()
            ) {
                Text("Guardar")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
