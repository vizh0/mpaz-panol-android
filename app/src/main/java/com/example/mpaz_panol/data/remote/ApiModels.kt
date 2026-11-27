package com.example.mpaz_panol.data.remote

import com.google.gson.annotations.SerializedName

data class EquipoDto(
    val id: Long? = null,
    val nombre: String,
    @SerializedName("categoria_id") val categoriaId: Long? = null,
    val marca: String? = "Genérica",
    val modelo: String? = "Estándar",
    @SerializedName("stock_total") val stockTotal: Int? = 1,
    val estado: String? = "Disponible"
)

data class SolicitudDto(
    val id: Long? = null,
    @SerializedName("usuario_id") val usuarioId: Long? = null,
    val salaUtilizacion: String,
    val fechaPlanificada: String,
    val estadoTicket: String,
    val fechaCreacion: String,
    @SerializedName("comentario_gestion") val comentarioGestion: String? = ""
)

data class UsuarioDto(
    val id: Long? = null,
    val rut: String,
    val nombre: String,
    val apellido: String,
    val correo: String? = null,
    val cargo: String = "Docente",
    val rol: String = "SOLICITANTE"
)