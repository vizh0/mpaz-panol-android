package com.example.mpaz_panol.ui.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_panol.data.local.entity.ItemEntity
import com.example.mpaz_panol.data.local.entity.RequestEntity
import com.example.mpaz_panol.data.local.entity.UserEntity
import com.example.mpaz_panol.data.repository.RequestRepository
import com.example.mpaz_panol.util.RutUtils
import com.example.mpaz_panol.util.ValidationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CartItem(val item: ItemEntity, val quantity: Int)

class RequestViewModel(private val repository: RequestRepository) : ViewModel() {

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage

    fun getRequests(user: UserEntity): Flow<List<RequestEntity>> {
        return if (user.role == "ADMIN") repository.getAllRequests()
        else repository.getRequestsForUser(user.id)
    }

    fun addToCart(item: ItemEntity) {
        val current = _cart.value.toMutableList()
        val idx = current.indexOfFirst { it.item.id == item.id }
        if (idx != -1) {
            val existing = current[idx]
            if (existing.quantity < item.availableStock) {
                current[idx] = existing.copy(quantity = existing.quantity + 1)
            }
        } else if (item.availableStock > 0) {
            current.add(CartItem(item, 1))
        }
        _cart.value = current
    }

    fun updateCartQuantity(itemId: Int, delta: Int) {
        val current = _cart.value.toMutableList()
        val idx = current.indexOfFirst { it.item.id == itemId }
        if (idx != -1) {
            val existing = current[idx]
            val newQty = existing.quantity + delta
            if (newQty <= 0) {
                current.removeAt(idx)
            } else if (newQty <= existing.item.availableStock) {
                current[idx] = existing.copy(quantity = newQty)
            }
        }
        _cart.value = current
    }

    fun removeFromCart(itemId: Int) {
        _cart.value = _cart.value.filterNot { it.item.id == itemId }
    }

    fun submitRequest(
        user: UserEntity,
        room: String,
        date: String,
        startTime: String,
        endTime: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (_cart.value.isEmpty()) {
                _feedbackMessage.value = "El carrito está vacío."
                return@launch
            }
            if (room.isBlank() || date.isBlank() || startTime.isBlank() || endTime.isBlank()) {
                _feedbackMessage.value = "Completa sala, fecha y horarios."
                return@launch
            }

            // Validación crítica de 3 horas de anticipación del caso MPAZ
            when (val validation = RutUtils.validateThreeHoursAnticipation(date, startTime)) {
                is ValidationResult.Error -> {
                    _feedbackMessage.value = validation.message
                    return@launch
                }
                ValidationResult.Success -> Unit
            }

            val summary = _cart.value.joinToString(", ") { "${it.quantity}x ${it.item.name}" }
            val rawData = _cart.value.joinToString(",") { "${it.item.id}:${it.quantity}" }

            val request = RequestEntity(
                userId = user.id,
                userName = user.fullName,
                userEmail = user.email,
                itemsSummary = summary,
                itemsData = rawData,
                room = room.trim(),
                date = date,
                startTime = startTime,
                endTime = endTime,
                status = "Pendiente"
            )
            repository.createRequest(request)
            _cart.value = emptyList()
            _feedbackMessage.value = null
            onSuccess()
        }
    }

    fun changeStatus(request: RequestEntity, newStatus: String, reason: String = "") {
        viewModelScope.launch {
            repository.updateRequestStatus(request, newStatus, reason)
        }
    }

    fun clearFeedback() {
        _feedbackMessage.value = null
    }
}