
package com.example.gamevaul.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamevaul.data.GameEntity

@Composable
fun AddGameDialog(
    game: GameEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        platform: String,
        category: String
    ) -> Unit
) {

    val platforms = listOf(
        "PC",
        "PlayStation",
        "Xbox",
        "Nintendo"
    )

    val categories = listOf(
        "Plataformas",
        "Sandbox",
        "Carreras",
        "Deportes",
        "Disparos"
    )

    var title by remember(game?.id) {
        mutableStateOf(game?.title ?: "")
    }

    var platform by remember(game?.id) {
        mutableStateOf(
            game?.platform?.takeIf { it in platforms } ?: "PC"
        )
    }

    var category by remember(game?.id) {
        mutableStateOf(
            when (game?.imageResName) {
                "platformer" -> "Plataformas"
                "carreras" -> "Carreras"
                "deportes" -> "Deportes"
                "disparos" -> "Disparos"
                else -> "Sandbox"
            }
        )
    }

    var expandedPlatform by remember {
        mutableStateOf(false)
    }

    var expandedCategory by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                if (game == null) "Agregar videojuego"
                else "Editar videojuego"
            )
        },

        text = {
            Column {

                // Nombre del videojuego
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text("Nombre del videojuego")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                // Desplegable de plataformas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    OutlinedTextField(
                        value = platform,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Plataforma")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedPlatform = true
                            },
                        trailingIcon = {
                            Text(
                                if (expandedPlatform) "▲" else "▼",
                                modifier = Modifier.clickable {
                                    expandedPlatform = !expandedPlatform
                                }
                            )
                        },
                        singleLine = true
                    )

                    DropdownMenu(
                        expanded = expandedPlatform,
                        onDismissRequest = {
                            expandedPlatform = false
                        },
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        platforms.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(option)
                                },
                                onClick = {
                                    platform = option
                                    expandedPlatform = false
                                }
                            )
                        }
                    }
                }

                // Desplegable de categorías
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Categoría")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedCategory = true
                            },
                        trailingIcon = {
                            Text(
                                if (expandedCategory) "▲" else "▼",
                                modifier = Modifier.clickable {
                                    expandedCategory = !expandedCategory
                                }
                            )
                        },
                        singleLine = true
                    )

                    DropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = {
                            expandedCategory = false
                        },
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        categories.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(option)
                                },
                                onClick = {
                                    category = option
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        title.trim(),
                        platform,
                        category
                    )
                },
                enabled = title.isNotBlank()
            ) {
                Text(
                    if (game == null) "Guardar"
                    else "Actualizar"
                )
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}
