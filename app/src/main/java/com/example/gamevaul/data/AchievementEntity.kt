
package com.example.gamevaul.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "achievements",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("gameId")]
)
data class AchievementEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val gameId: Int,

    val name: String,

    val description: String,

    val isCompleted: Boolean = false
)
