package com.example.mpaz_panol.data.repository

import com.example.mpaz_panol.data.local.dao.UserDao
import com.example.mpaz_panol.data.local.entity.UserEntity

class UserRepository(private val userDao: UserDao) {
    suspend fun login(email: String, password: String): UserEntity? =
        userDao.login(email.trim().lowercase(), password)

    suspend fun getUserById(id: Int): UserEntity? = userDao.getUserById(id)

    suspend fun register(fullName: String, rut: String, email: String, password: String): Result<UserEntity> {
        val cleanEmail = email.trim().lowercase()
        if (userDao.getUserByEmail(cleanEmail) != null) {
            return Result.failure(Exception("El correo ya está registrado."))
        }
        // DETECCIÓN AUTOMÁTICA DE ROL POR DOMINIO
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
        return Result.success(newUser.copy(id = id))
    }
}