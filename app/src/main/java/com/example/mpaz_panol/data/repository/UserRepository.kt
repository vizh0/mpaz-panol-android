package com.example.mpaz_panol.data.repository

import com.example.mpaz_panol.data.local.dao.UserDao
import com.example.mpaz_panol.data.local.entity.UserEntity
import com.example.mpaz_panol.data.remote.RetrofitClient
import com.example.mpaz_panol.data.remote.UsuarioDto

class UserRepository(private val userDao: UserDao) {
    suspend fun login(email: String, password: String): UserEntity? =
        userDao.login(email.trim().lowercase(), password)

    suspend fun getUserById(id: Int): UserEntity? = userDao.getUserById(id)

    suspend fun register(fullName: String, rut: String, email: String, password: String): Result<UserEntity> {
        val cleanEmail = email.trim().lowercase()
        if (userDao.getUserByEmail(cleanEmail) != null) {
            return Result.failure(Exception("El correo ya está registrado."))
        }
        val assignedRole = if (cleanEmail.endsWith("@panol.mpaz.cl") || cleanEmail.endsWith("@admin.mpaz.cl")) {
            "ADMIN"
        } else {
            "SOLICITANTE"
        }

        val newUser = UserEntity(
            fullName = fullName.trim(),
            rut = rut.trim(),
            email = cleanEmail,
            password = password,
            role = assignedRole
        )
        val id = userDao.insertUser(newUser).toInt()

        try {
            val partes = fullName.trim().split(" ", limit = 2)
            val nombre = partes.getOrElse(0) { fullName.trim() }
            val apellido = partes.getOrElse(1) { "Sin Apellido" }

            RetrofitClient.usuariosApi.crearUsuario(
                UsuarioDto(
                    rut = newUser.rut,
                    nombre = nombre,
                    apellido = apellido,
                    correo = newUser.email,
                    cargo = if (assignedRole == "ADMIN") "Encargado Pañol" else "Docente",
                    rol = assignedRole
                )
            )
        } catch (_: Exception) { }

        return Result.success(newUser.copy(id = id))
    }
}