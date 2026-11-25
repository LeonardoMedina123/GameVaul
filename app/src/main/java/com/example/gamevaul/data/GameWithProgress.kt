
package com.example.gamevaul.data

data class GameWithProgress(
    val id: Int,
    val title: String,
    val platform: String,
    val imageResName: String,
    val completedCount: Int,
    val totalCount: Int
) {
    val progress: Int
        get() = if (totalCount == 0) {
            0
        } else {
            (completedCount * 100) / totalCount
        }

    val status: String
        get() = if (totalCount > 0 && completedCount == totalCount) {
            "Completado"
        } else {
            "Pendiente"
        }
}
