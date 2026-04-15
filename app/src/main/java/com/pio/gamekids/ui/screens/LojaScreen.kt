package com.pio.gamekids.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pio.gamekids.data.model.Recompensa
import com.pio.gamekids.viewmodel.LojaViewModel

val LaranjaLoja = Color(0xFFF5A623)

@Composable
fun LojaScreen(
    criancaId: String,
    saldoAtual: Int,
    onNavigateBack: () -> Unit,
    viewModel: LojaViewModel = viewModel()
) {
    val recompensas by viewModel.recompensas.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LaranjaLoja)
            .padding(20.dp)
    ) {
        // Cabeçalho da Loja
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.background(Color.White.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Voltar", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Loja de Prêmios",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f)
            )
        }

        // Saldo Atual
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Seu Saldo: ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Icon(Icons.Rounded.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("$saldoAtual", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
            }
        }

        // Grid de Produtos
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recompensas) { item ->
                ItemLojaCard(
                    item = item,
                    saldoSuficiente = saldoAtual >= item.custo,
                    onClick = { viewModel.comprarItem(context, criancaId, saldoAtual, item) }
                )
            }
        }
    }
}

@Composable
fun ItemLojaCard(item: Recompensa, saldoSuficiente: Boolean, onClick: () -> Unit) {
    val corFundo = if (saldoSuficiente) Color.White else Color.White.copy(alpha = 0.6f)
    val corTexto = if (saldoSuficiente) Color.DarkGray else Color.Gray

    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(0.85f).clickable(enabled = saldoSuficiente, onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = corFundo),
        elevation = CardDefaults.cardElevation(defaultElevation = if (saldoSuficiente) 6.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = item.icone, fontSize = 48.sp)
            Text(
                text = item.titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = corTexto,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Star, contentDescription = null, tint = LaranjaLoja, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${item.custo}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = LaranjaLoja)
            }
        }
    }
}