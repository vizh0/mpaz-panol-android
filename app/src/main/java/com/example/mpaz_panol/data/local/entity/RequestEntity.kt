package com.example.mpaz_panol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests")
data class RequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val userName: String,
    val userEmail: String,
    val itemsSummary: String, // Ej: "2x Proyector Epson, 1x Alargador 10m"
    val itemsData: String,    // Formato interno: "id:qty,id:qty" para descontar stock real
    val room: String,
    val date: String,         // Formato "dd/MM/yyyy"
    val startTime: String,    // Formato "HH:mm"
    val endTime: String,      // Formato "HH:mm"
    val status: String,       // Pendiente, Aceptada, Rechazada, Entregada, Devuelta
    val rejectionReason: String = "",
    val createdAt: Long = System.currentTimeMillis()
)