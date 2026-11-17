package com.example.mpaz_panol.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_panol.data.local.entity.UserEntity
import com.example.mpaz_panol.ui.components.FooterBar
import com.example.mpaz_panol.ui.components.HeaderBar

@Composable
fun HomeScreen(
    user: UserEntity,
    onNavigateToCatalog: () -> Unit,
    onNavigateToRequest: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            HeaderBar(
                title = user.fullName,
                subtitle = if (user.role == "ADMIN") "Administrador Pañol TI" else "Docente / Solicitante",
                onLogout = onLogout
            )
        },
        bottomBar = {
            FooterBar(
                currentRoute = "home",
                userRole = user.role,
                onNavigateToHome = {},
                onNavigateToCatalog = onNavigateToCatalog,
                onNavigateToRequest = onNavigateToRequest,
                onNavigateToHistory = onNavigateToHistory
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MenuCard(
                title = if (user.role == "ADMIN") "Gestionar Catálogo" else "Catálogo del Pañol",
                subtitle = if (user.role == "ADMIN") "Agregar, editar y dar de baja equipos" else "Revisar stock, categoría y ubicación",
                icon = Icons.Default.Inventory,
                onClick = onNavigateToCatalog
            )

            if (user.role != "ADMIN") {
                Spacer(Modifier.height(16.dp))
                MenuCard(
                    title = "Solicitar Equipos",
                    subtitle = "Armar carrito y reservar para sala",
                    icon = Icons.Default.AddShoppingCart,
                    onClick = onNavigateToRequest
                )
            }

            Spacer(Modifier.height(16.dp))
            MenuCard(
                title = if (user.role == "ADMIN") "Auditar Solicitudes" else "Historial de Solicitudes",
                subtitle = if (user.role == "ADMIN") "Aprobar, rechazar o marcar entregas" else "Ver estados y justificaciones",
                icon = Icons.Default.History,
                onClick = onNavigateToHistory
            )
        }
    }
}

@Composable
private fun MenuCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}