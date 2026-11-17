package com.example.mpaz_panol.ui.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_panol.data.local.entity.ItemEntity
import com.example.mpaz_panol.data.local.entity.UserEntity
import com.example.mpaz_panol.ui.components.FooterBar
import com.example.mpaz_panol.ui.components.HeaderBar

@Composable
fun CatalogScreen(
    user: UserEntity,
    viewModel: InventoryViewModel,
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToRequest: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val items by viewModel.filteredItems.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    var editingItem by remember { mutableStateOf<ItemEntity?>(null) }
    var showFormDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { HeaderBar(title = "Catálogo del Pañol", onBack = onBack) },
        bottomBar = {
            FooterBar(
                currentRoute = "catalog",
                userRole = user.role,
                onNavigateToHome = onNavigateToHome,
                onNavigateToCatalog = {},
                onNavigateToRequest = onNavigateToRequest,
                onNavigateToHistory = onNavigateToHistory
            )
        },
        floatingActionButton = {
            if (user.role == "ADMIN") {
                FloatingActionButton(onClick = {
                    editingItem = null
                    showFormDialog = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Equipo")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onSearchChange(it) },
                label = { Text("Buscar por nombre o categoría...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(items) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                if (user.role == "ADMIN") {
                                    IconButton(onClick = {
                                        editingItem = item
                                        showFormDialog = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                    }
                                    IconButton(onClick = { viewModel.deleteItem(item) }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                            Text("Categoría: ${item.category}", style = MaterialTheme.typography.bodyMedium)
                            Text("Ubicación: ${item.location}", style = MaterialTheme.typography.bodyMedium)
                            Text("Descripción: ${item.description}", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Stock: ${item.availableStock} / ${item.totalStock} (${item.status})",
                                fontWeight = FontWeight.SemiBold,
                                color = if (item.availableStock > 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }

        if (showFormDialog) {
            ItemFormDialog(
                item = editingItem,
                onDismiss = { showFormDialog = false },
                onSave = { saved ->
                    viewModel.saveItem(saved)
                    showFormDialog = false
                }
            )
        }
    }
}

@Composable
private fun ItemFormDialog(
    item: ItemEntity?,
    onDismiss: () -> Unit,
    onSave: (ItemEntity) -> Unit
) {
    var name by remember { mutableStateOf(item?.name ?: "") }
    var description by remember { mutableStateOf(item?.description ?: "") }
    var category by remember { mutableStateOf(item?.category ?: "") }
    var totalStock by remember { mutableStateOf(item?.totalStock?.toString() ?: "1") }
    var status by remember { mutableStateOf(item?.status ?: "Disponible") }
    var location by remember { mutableStateOf(item?.location ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Nuevo Equipo" else "Editar Equipo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") })
                OutlinedTextField(value = totalStock, onValueChange = { totalStock = it }, label = { Text("Cantidad Total") })
                OutlinedTextField(value = status, onValueChange = { status = it }, label = { Text("Estado (Disponible/Mantenimiento)") })
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Ubicación física") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val stockInt = totalStock.toIntOrNull() ?: 1
                onSave(
                    ItemEntity(
                        id = item?.id ?: 0,
                        name = name,
                        description = description,
                        category = category,
                        totalStock = stockInt,
                        availableStock = stockInt,
                        status = status,
                        location = location
                    )
                )
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}