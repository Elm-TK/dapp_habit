package com.example.dz3

data class Habit(
    val id: Int,
    val color: Int,
    var title: String,
    val description: String,
    val priority: String,
    val type: String,
    val repeat: Int,
    val days: Int
) : java.io.Serializable {
}
