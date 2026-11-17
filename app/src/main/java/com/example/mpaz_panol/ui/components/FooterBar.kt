package com.example.mpaz_panol.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun FooterBar(
    currentRoute: String,
    userRole: String,
    onNavigateToHome: () -> Unit,
    onNavigateToCatalog: () -> Unit,
    onNavigateToRequest: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = onNavigateToHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = currentRoute == "catalog",
            onClick = onNavigateToCatalog,
            icon = { Icon(Icons.Default.Inventory, contentDescription = "Catálogo") },
            label = { Text("Catálogo") }
        )
        if (userRole != "ADMIN") {
            NavigationBarItem(
                selected = currentRoute == "request",
                onClick = onNavigateToRequest,
                icon = { Icon(Icons.Default.AddShoppingCart, contentDescription = "Solicitar") },
                label = { Text("Solicitar") }
            )
        }
        NavigationBarItem(
            selected = currentRoute == "history",
            onClick = onNavigateToHistory,
            icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
            label = { Text("Historial") }
        )
    }
}