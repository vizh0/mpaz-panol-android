package com.example.mpazpanol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

    Scaffold(
        topBar = { Header(title = "PAÑOL - CATÁLOGO PRINCIPAL") },
        bottomBar = { Footer() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Botón de cerrar sesión en la parte superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text("Cerrar Sesión", color = Color.White)
                }
            }

            // Muestra la pantalla del Catálogo justo abajo
            CatalogoScreen()
        }
    }
}