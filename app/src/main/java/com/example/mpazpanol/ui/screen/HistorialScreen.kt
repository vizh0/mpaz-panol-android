package com.example.mpazpanol.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mpazpanol.model.SolicitudModel

@Composable
fun HistorialScreen() {
    val solicitudes = remember {
        listOf(
            SolicitudModel(101, "Notebook Lenovo ThinkPad", "Lab Robótica 2", 4, "Pendiente"),
            SolicitudModel(100, "Proyector Epson PowerLite", "Sala 8B", 5, "Aceptada"),
            SolicitudModel(99, "Kit Robótica LEGO", "Lab Computación", 1, "Rechazada - Fuera de plazo")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mis Solicitudes y Tickets", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(solicitudes) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Ticket #${item.id}: ${item.equipoNombre}", style = MaterialTheme.typography.titleSmall)
                        Text(text = "Lugar: ${item.sala}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Estado: ${item.estado}",
                            color = when (item.estado) {
                                "Aceptada" -> Color(0xFF2E7D32)
                                "Rechazada - Fuera de plazo" -> Color.Red
                                else -> Color(0xFFF57C00)
                            }
                        )
                    }
                }
            }
        }
    }
}