package com.example.mpaz_panol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val category: String, // Computador, Notebook, Proyector, Alargador, Parlante, Robótica, Red
    val totalStock: Int,
    val availableStock: Int,
    val status: String, // Disponible, Reservado, Prestado, En Mantenimiento, Fuera de Servicio
    val location: String
)