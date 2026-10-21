package com.example.mpazpanol.ui.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mpazpanol.ui.PañolUtils

@Composable
fun CarritoScreen(
    onSolicitudEnviada: () -> Unit
) {
    val context = LocalContext.current
    var sala by remember { mutableStateOf("") }
    var horasTexto by remember { mutableStateOf("3") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Formulario de Solicitud (Carrito)", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Equipo: Notebook Lenovo ThinkPad", style = MaterialTheme.typography.titleSmall)
                Text("Cantidad: 1 unidad", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = sala,
            onValueChange = { sala = it },
            label = { Text("Sala o Laboratorio de uso") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = horasTexto,
            onValueChange = { horasTexto = it },
            label = { Text("Horas de anticipación para el uso") },
            modifier = Modifier.fillMaxWidth()
        )

        // Animaciones de Material 3 para retroalimentación visual
        AnimatedVisibility(visible = mensajeError != null) {
            Text(
                text = mensajeError ?: "",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val horas = horasTexto.toIntOrNull() ?: 0
                if (sala.isBlank()) {
                    mensajeError = "Por favor ingresa la sala."
                    PañolUtils.vibrar(context)
                } else if (horas < 3) {
                    // Regla de Negocio Crítica: Bloqueo si es menor a 3 horas
                    mensajeError = "❌ Bloqueado: Se exige mínimo 3 horas de anticipación."
                    PañolUtils.vibrar(context)
                } else {
                    mensajeError = null
                    PañolUtils.vibrar(context)
                    PañolUtils.mostrarNotificacion(
                        context,
                        "Solicitud Creada",
                        "Tu pedido para $sala ha sido enviado a revisión."
                    )
                    Toast.makeText(context, "¡Solicitud registrada correctamente!", Toast.LENGTH_SHORT).show()
                    onSolicitudEnviada()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar y Enviar Solicitud")
        }
    }
}