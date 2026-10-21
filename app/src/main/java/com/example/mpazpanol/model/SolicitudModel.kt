package com.example.mpazpanol.model

data class SolicitudModel(
    val id: Int,
    val equipoNombre: String,
    val sala: String,
    val horasAnticipacion: Int,
    val estado: String // Pendiente, Aceptada, Rechazada
)