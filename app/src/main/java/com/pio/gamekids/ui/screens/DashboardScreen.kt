package com.pio.gamekids.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pio.gamekids.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel, onTaskClick: (String) -> Unit) {
    val crianca by viewModel.crianca.collectAsState()
    val atividades by viewModel.atividades.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF4A90E2)).padding(16.dp)) {
        // Header
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Olá, ${crianca.nome}! 👋", fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Box(modifier = Modifier.clip(RoundedCornerShape(50)).background(Color(0xFFFFD700)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.White)
                    Text("${crianca.saldo_atual}", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(atividades) { atividade ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onTaskClick(atividade.id) },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(atividade.titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("+${atividade.valor} moedas", color = Color(0xFFD4AF37))
                        }
                        Button(onClick = { onTaskClick(atividade.id) }) {
                            Text("Fazer!")
                        }
                    }
                }
            }
        }
    }
}