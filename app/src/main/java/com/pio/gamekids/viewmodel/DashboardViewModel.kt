// Local: /app/src/main/java/com/pio/gamekids/viewmodel/DashboardViewModel.kt
package com.pio.gamekids.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.pio.gamekids.data.model.Atividade
import com.pio.gamekids.data.model.Crianca
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // TODO: Este ID da família virá do fluxo de Login (Firebase Auth) futuramente
    private val familiaId = "id_familia_exemplo"

    private val _crianca = MutableStateFlow(Crianca())
    val crianca: StateFlow<Crianca> = _crianca

    private val _atividades = MutableStateFlow<List<Atividade>>(emptyList())
    val atividades: StateFlow<List<Atividade>> = _atividades

    // Controle para não recarregar os mesmos dados e para limpar ouvintes antigos
    private var idCriancaAtual: String? = null
    private var listenerCrianca: ListenerRegistration? = null
    private var listenerAtividades: ListenerRegistration? = null

    fun carregarDadosPara(criancaSelecionadaId: String) {
        // Evita recarregar os dados se o ID for vazio ou se for a mesma criança
        if (criancaSelecionadaId.isEmpty() || criancaSelecionadaId == idCriancaAtual) return

        idCriancaAtual = criancaSelecionadaId

        // Limpa os listeners antigos (Importante: evita bugs se trocar de perfil rapidamente)
        listenerCrianca?.remove()
        listenerAtividades?.remove()

        val criancaRef = db.collection("usuarios")
            .document(familiaId)
            .collection("criancas")
            .document(criancaSelecionadaId)

        // 1. Escuta alterações no perfil da Criança (Moedas, Nome)
        listenerCrianca = criancaRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("Firestore", "Erro ao buscar dados da criança", error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val dados = snapshot.toObject(Crianca::class.java)
                dados?.let { _crianca.value = it }
            }
        }

        // 2. Escuta a lista de Atividades Pendentes
        listenerAtividades = criancaRef.collection("atividades")
            .whereEqualTo("status", "pendente")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("Firestore", "Erro ao buscar atividades", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val lista = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Atividade::class.java)?.copy(id = doc.id)
                    }
                    _atividades.value = lista
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        // Boa prática: remove os ouvintes do Firestore quando a tela é destruída
        listenerCrianca?.remove()
        listenerAtividades?.remove()
    }
}