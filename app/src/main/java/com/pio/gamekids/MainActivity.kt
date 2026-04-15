// Local: /app/src/main/java/com/pio/gamekids/MainActivity.kt
package com.pio.gamekids

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pio.gamekids.ui.screens.CameraScreen
import com.pio.gamekids.ui.screens.DashboardScreen
import com.pio.gamekids.ui.screens.LojaScreen
import com.pio.gamekids.ui.screens.PerfilScreen
import com.pio.gamekids.ui.theme.GameKidsTheme
import com.pio.gamekids.viewmodel.DashboardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameKidsTheme {
                val navController = rememberNavController()

                // O app agora inicia na tela de seleção de perfil
                NavHost(navController = navController, startDestination = "selecao_perfil") {

                    // 1. Tela de Seleção de Perfil
                    composable("selecao_perfil") {
                        PerfilScreen(onPerfilSelecionado = { id ->
                            // Vai para o dashboard passando o ID da criança escolhida
                            navController.navigate("dashboard/$id")
                        })
                    }

                    // 2. Tela Principal (Dashboard)
                    composable("dashboard/{criancaId}") { backStackEntry ->
                        val criancaId = backStackEntry.arguments?.getString("criancaId") ?: ""
                        val vm: DashboardViewModel = viewModel()

                        // Avisa o ViewModel qual criança carregar
                        vm.carregarDadosPara(criancaId)

                        DashboardScreen(
                            viewModel = vm,
                            onTaskClick = { taskId ->
                                // CORRIGIDO: Agora passa o criancaId e o taskId
                                navController.navigate("camera/$criancaId/$taskId")
                            },
                            onStoreClick = { saldoAtual ->
                                // NOVA NAVEGAÇÃO: Abre a loja passando o saldo
                                navController.navigate("loja/$criancaId/$saldoAtual")
                            }
                        )
                    }

                    // 3. Tela da Câmera
                    composable("camera/{criancaId}/{taskId}") { backStackEntry ->
                        val criancaId = backStackEntry.arguments?.getString("criancaId") ?: ""
                        val taskId = backStackEntry.arguments?.getString("taskId") ?: ""

                        CameraScreen(
                            criancaId = criancaId,
                            taskId = taskId,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 4. Tela da Loja de Prêmios
                    composable("loja/{criancaId}/{saldo}") { backStackEntry ->
                        val criancaId = backStackEntry.arguments?.getString("criancaId") ?: ""
                        val saldo = backStackEntry.arguments?.getString("saldo")?.toIntOrNull() ?: 0

                        LojaScreen(
                            criancaId = criancaId,
                            saldoAtual = saldo,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}