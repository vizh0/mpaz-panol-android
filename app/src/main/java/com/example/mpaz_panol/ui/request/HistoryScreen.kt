package com.example.mpaz_panol.ui.request

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_panol.data.local.entity.RequestEntity
import com.example.mpaz_panol.data.local.entity.UserEntity
import com.example.mpaz_panol.ui.components.FooterBar
import com.example.mpaz_panol.ui.components.HeaderBar

@Composable
fun HistoryScreen(
    user: UserEntity,
    requestViewModel: RequestViewModel,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToCatalog: () -> Unit,
    onNavigateToRequest: () -> Unit
) {
    val requests by requestViewModel.getRequests(user).collectAsState(initial = emptyList())
    var requestToReject by remember { mutableStateOf<RequestEntity?>(null) }
    var rejectionReason by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            HeaderBar(
                title = if (user.role == "ADMIN") "Consola de Solicitudes" else "Historial de Solicitudes",
                onBack = onBack
            )
        },
        bottomBar = {
            FooterBar(
                currentRoute = "history",
                userRole = user.role,
                onNavigateToHome = onNavigateToHome,
                onNavigateToCatalog = onNavigateToCatalog,
                onNavigateToRequest = onNavigateToRequest,
                onNavigateToHistory = {}
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(requests) { req ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Ticket #${req.id} - ${req.status}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (user.role == "ADMIN") {
                            Text("Solicitante: ${req.userName} (${req.userEmail})", style = MaterialTheme.typography.bodySmall)
                        }
                        Text("Equipos: ${req.itemsSummary}", style = MaterialTheme.typography.bodyMedium)
                        Text("Lugar: ${req.room} | Fecha: ${req.date} (${req.startTime} - ${req.endTime})", style = MaterialTheme.typography.bodySmall)

                        if (req.status == "Rechazada" && req.rejectionReason.isNotBlank()) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Justificación Pañol: ${req.rejectionReason}",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        if (user.role == "ADMIN") {
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (req.status == "Pendiente") {
                                    Button(onClick = { requestViewModel.changeStatus(req, "Aceptada") }) {
                                        Text("Aceptar")
                                    }
                                    OutlinedButton(onClick = {
                                        rejectionReason = ""
                                        requestToReject = req
                                    }) {
                                        Text("Rechazar")
                                    }
                                } else if (req.status == "Aceptada") {
                                    Button(onClick = { requestViewModel.changeStatus(req, "Entregada") }) {
                                        Text("Marcar Entregada")
                                    }
                                } else if (req.status == "Entregada") {
                                    Button(onClick = { requestViewModel.changeStatus(req, "Devuelta") }) {
                                        Text("Marcar Devuelta")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (requestToReject != null) {
            AlertDialog(
                onDismissRequest = { requestToReject = null },
                title = { Text("Justificación de Rechazo") },
                text = {
                    OutlinedTextField(
                        value = rejectionReason,
                        onValueChange = { rejectionReason = it },
                        label = { Text("Motivo obligatorio del rechazo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        enabled = rejectionReason.isNotBlank(),
                        onClick = {
                            requestViewModel.changeStatus(requestToReject!!, "Rechazada", rejectionReason.trim())
                            requestToReject = null
                        }
                    ) { Text("Confirmar Rechazo") }
                },
                dismissButton = {
                    TextButton(onClick = { requestToReject = null }) { Text("Cancelar") }
                }
            )
        }
    }
}