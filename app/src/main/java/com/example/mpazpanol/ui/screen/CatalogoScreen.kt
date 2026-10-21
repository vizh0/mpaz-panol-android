package com.example.mpazpanol.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mpazpanol.model.EquipoModel

@Composable
fun CatalogoScreen() {
    val equipos = remember {
        listOf(
            EquipoModel(1, "Proyector Epson PowerLite", "Proyector", 5, 3, "Disponible"),
            EquipoModel(2, "Notebook Lenovo ThinkPad", "Notebook", 15, 10, "Disponible"),
            EquipoModel(3, "Kit Robótica LEGO WeDo 2.0", "Robótica", 8, 4, "Disponible"),
            EquipoModel(4, "Placa ESP32 DevKit", "Robótica", 20, 18, "Disponible"),
            EquipoModel(5, "Switch Cisco 24 Puertos", "Equipamiento Red", 2, 0, "En Mantenimiento")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Equipos Disponibles en Pañol", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(equipos) { equipo ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = equipo.nombre, style = MaterialTheme.typography.titleSmall)
                        Text(text = "Categoría: ${equipo.categoria}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Disponible: ${equipo.stockDisponible} / ${equipo.stockTotal}",
                            color = if (equipo.stockDisponible > 0) Color(0xFF2E7D32) else Color.Red
                        )
                    }
                }
            }
        }
    }
}