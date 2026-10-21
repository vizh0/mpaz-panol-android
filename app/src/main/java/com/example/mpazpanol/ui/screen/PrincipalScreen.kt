package com.example.mpazpanol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mpazpanol.ui.components.Footer
import com.example.mpazpanol.ui.components.Header

@Composable
fun PrincipalScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var pestanaSeleccionada by remember { mutableStateOf(0) }

    Scaffold(
        topBar = { Header(title = "PAÑOL - ESCUELA MARCELA PAZ") },
        bottomBar = { Footer() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { pestanaSeleccionada = 0 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (pestanaSeleccionada == 0) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Text("Catálogo")
                }

                Button(
                    onClick = { pestanaSeleccionada = 1 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (pestanaSeleccionada == 1) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Text("Carrito")
                }

                Button(
                    onClick = { pestanaSeleccionada = 2 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (pestanaSeleccionada == 2) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Text("Historial")
                }

                Button(
                    onClick = {
                        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text("Salir")
                }
            }

            // Muestra dinámicamente el composable correspondiente
            when (pestanaSeleccionada) {
                0 -> CatalogoScreen()
                1 -> CarritoScreen(onSolicitudEnviada = { pestanaSeleccionada = 2 })
                2 -> HistorialScreen()
            }
        }
    }
}