package com.example.mpaz_panol.ui.request

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_panol.data.local.entity.UserEntity
import com.example.mpaz_panol.ui.components.FooterBar
import com.example.mpaz_panol.ui.components.HeaderBar
import com.example.mpaz_panol.ui.inventory.InventoryViewModel
import com.example.mpaz_panol.util.NativeResourcesHelper
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestScreen(
    user: UserEntity,
    inventoryViewModel: InventoryViewModel,
    requestViewModel: RequestViewModel,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToCatalog: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onRequestSubmitted: () -> Unit
) {
    val context = LocalContext.current
    val availableItems by inventoryViewModel.filteredItems.collectAsState()
    val cart by requestViewModel.cart.collectAsState()
    val feedback by requestViewModel.feedbackMessage.collectAsState()

    var room by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    Scaffold(
        topBar = { HeaderBar(title = "Nueva Solicitud", onBack = onBack) },
        bottomBar = {
            FooterBar(
                currentRoute = "request",
                userRole = user.role,
                onNavigateToHome = onNavigateToHome,
                onNavigateToCatalog = onNavigateToCatalog,
                onNavigateToRequest = {},
                onNavigateToHistory = onNavigateToHistory
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("1. Selecciona Equipos Disponibles", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            availableItems.filter { it.availableStock > 0 && it.status == "Disponible" }.forEach { item ->
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.name, fontWeight = FontWeight.SemiBold)
                            Text("Disp: ${item.availableStock} | ${item.category}", style = MaterialTheme.typography.bodySmall)
                        }
                        Button(onClick = { requestViewModel.addToCart(item) }) {
                            Text("+ Agregar")
                        }
                    }
                }
            }

            HorizontalDivider()
            Text("2. Carrito de Solicitud (${cart.sumOf { it.quantity }} ítems)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            if (cart.isEmpty()) {
                Text("Aún no agregas equipos al carrito.", style = MaterialTheme.typography.bodySmall)
            } else {
                cart.forEach { cartItem ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(cartItem.item.name, modifier = Modifier.weight(1f))
                            IconButton(onClick = { requestViewModel.updateCartQuantity(cartItem.item.id, -1) }) {
                                Icon(Icons.Default.Remove, contentDescription = "Restar")
                            }
                            Text("${cartItem.quantity}", fontWeight = FontWeight.Bold)
                            IconButton(onClick = { requestViewModel.updateCartQuantity(cartItem.item.id, 1) }) {
                                Icon(Icons.Default.Add, contentDescription = "Sumar")
                            }
                            IconButton(onClick = { requestViewModel.removeFromCart(cartItem.item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Quitar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            HorizontalDivider()
            Text("3. Datos de Uso (Mín. 3 hrs anticipación)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = room,
                onValueChange = { room = it },
                label = { Text("Sala o Lugar (ej: Sala 4°B / Lab Computación)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (selectedDate.isEmpty()) "Seleccionar Fecha en Calendario" else "Fecha: $selectedDate")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Hora Inicio (HH:mm)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("Devolución (HH:mm)") },
                    modifier = Modifier.weight(1f)
                )
            }

            if (feedback != null) {
                Text(feedback!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Button(
                onClick = {
                    val summaryCopy = cart.joinToString(", ") { "${it.quantity}x ${it.item.name}" }
                    requestViewModel.submitRequest(user, room, selectedDate, startTime, endTime) {
                        NativeResourcesHelper.triggerVibration(context)
                        NativeResourcesHelper.exportToNativeCalendar(
                            context = context,
                            title = summaryCopy,
                            location = room,
                            description = "Devolución de equipos en Pañol MPAZ",
                            dateStr = selectedDate,
                            startTimeStr = startTime,
                            endTimeStr = endTime
                        )
                        onRequestSubmitted()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Solicitud y Agendar en Calendario")
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                            selectedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        }
                        showDatePicker = false
                    }) { Text("Aceptar") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}