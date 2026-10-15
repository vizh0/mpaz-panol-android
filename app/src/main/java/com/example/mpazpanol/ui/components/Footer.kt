package com.example.mpazpanol.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Footer(copyrightText: String = "© 2026 Escuela Marcela Paz - Pañol TI") {
    Box(
        modifier = Modifier.fillMaxWidth().height(48.dp).background(Color(0xFF101010)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = copyrightText, color = Color.Gray, fontSize = 12.sp)
    }
}