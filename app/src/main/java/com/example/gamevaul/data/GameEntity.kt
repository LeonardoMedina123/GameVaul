
package com.example.gamevaul.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val platform: String,

    val description: String = "",

    val status: String = "Pendiente",

    val progress: Int = 0,

    val imageResName: String = "game_default"
)
