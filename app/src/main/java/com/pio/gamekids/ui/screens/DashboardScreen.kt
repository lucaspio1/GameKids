package com.pio.gamekids.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Star
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

// Paleta de Cores Infantil
val AzulSuave = Color(0xFF4A90E2)
val AmareloVibrante = Color(0xFFFFD700)
val VerdeConquista = Color(0xFF7ED321)

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onTaskClick: (String) -> Unit,
    onStoreClick: (Int) -> Unit
) {
    val crianca by viewModel.crianca.collectAsState()
    val atividades by viewModel.atividades.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulSuave)
            .padding(20.dp)
    ) {
        // Cabeçalho Lúdico
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Olá,", fontSize = 20.sp, color = Color.White.copy(alpha = 0.8f))
                Text("${crianca.nome}! 👋", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
            }

            // Badge de Moedas (Amarelo Vibrante)
            Surface(
                shape = CircleShape,
                color = AmareloVibrante,
                shadowElevation = 4.dp,
                modifier = Modifier.clickable { onStoreClick(crianca.saldo_atual) } // <-- Adicione esta linha
            ){
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Star, contentDescription = "Moedas", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${crianca.saldo_atual}",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Text(
            text = "Suas Missões",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de Tarefas
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(atividades) { atividade ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTaskClick(atividade.id) },
                    shape = RoundedCornerShape(24.dp), // Bordas bem arredondadas (Kids)
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = atividade.titulo,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "+${atividade.valor} moedas",
                                color = AmareloVibrante,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        // Botão Verde de Ação
                        IconButton(
                            onClick = { onTaskClick(atividade.id) },
                            modifier = Modifier
                                .size(56.dp)
                                .background(VerdeConquista, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = "Fazer Tarefa",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}