package com.example.mpaz_panol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val rut: String,
    val email: String,
    val password: String,
    val role: String // "ADMIN" o "SOLICITANTE"
)