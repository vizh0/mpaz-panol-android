package com.example.mpaz_panol.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_panol.data.local.SessionManager
import com.example.mpaz_panol.data.local.entity.UserEntity
import com.example.mpaz_panol.data.repository.UserRepository
import com.example.mpaz_panol.util.RutUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError

    private val _isSessionChecked = MutableStateFlow(false)
    val isSessionChecked: StateFlow<Boolean> = _isSessionChecked

    init {
        restoreSession()
    }

    private fun restoreSession() {
        viewModelScope.launch {
            val savedId = sessionManager.getLoggedUserId()
            if (savedId != -1) {
                _currentUser.value = userRepository.getUserById(savedId)
            }
            _isSessionChecked.value = true
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authError.value = null
            if (email.isBlank() || password.isBlank()) {
                _authError.value = "Completa todos los campos."
                return@launch
            }
            val user = userRepository.login(email, password)
            if (user != null) {
                sessionManager.saveSession(user.id)
                _currentUser.value = user
                onSuccess()
            } else {
                _authError.value = "Credenciales incorrectas."
            }
        }
    }

    fun register(fullName: String, rut: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authError.value = null
            if (fullName.isBlank() || rut.isBlank() || email.isBlank() || password.length < 4) {
                _authError.value = "Completa todos los campos (clave mín. 4 caracteres)."
                return@launch
            }
            if (!RutUtils.isValidRut(rut)) {
                _authError.value = "El RUT ingresado no es válido."
                return@launch
            }
            val result = userRepository.register(fullName, rut, email, password)
            result.onSuccess { user ->
                sessionManager.saveSession(user.id)
                _currentUser.value = user
                onSuccess()
            }.onFailure {
                _authError.value = it.message
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        sessionManager.clearSession()
        _currentUser.value = null
        _authError.value = null
        onLoggedOut()
    }

    fun clearError() {
        _authError.value = null
    }
}