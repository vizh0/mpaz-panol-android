package com.example.mpazpanol.model

data class EquipoModel(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val stockTotal: Int,
    val stockDisponible: Int,
    val estado: String
)