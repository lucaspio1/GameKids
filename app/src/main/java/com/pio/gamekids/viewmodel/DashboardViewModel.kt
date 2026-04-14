package com.pio.gamekids.viewmodel

import androidx.lifecycle.ViewModel
import com.pio.gamekids.data.model.Atividade
import com.pio.gamekids.data.model.Crianca
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel : ViewModel() {
    private val _crianca = MutableStateFlow(Crianca(nome = "Herói", saldo_atual = 0))
    val crianca: StateFlow<Crianca> = _crianca

    private val _atividades = MutableStateFlow<List<Atividade>>(emptyList())
    val atividades: StateFlow<List<Atividade>> = _atividades

    init {
        _atividades.value = listOf(
            Atividade(id = "1", titulo = "Arrumar a cama", valor = 10),
            Atividade(id = "2", titulo = "Dever de casa", valor = 20)
        )
    }
}