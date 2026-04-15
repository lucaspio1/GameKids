// Local: /app/src/main/java/com/pio/gamekids/viewmodel/PerfilViewModel.kt
package com.pio.gamekids.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pio.gamekids.data.model.Crianca
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PerfilViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val familiaId = "id_familia_exemplo" // Integrar com Auth futuramente

    private val _perfis = MutableStateFlow<List<Pair<String, Crianca>>>(emptyList())
    val perfis: StateFlow<List<Pair<String, Crianca>>> = _perfis

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        carregarPerfis()
    }

    private fun carregarPerfis() {
        db.collection("usuarios").document(familiaId).collection("criancas")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val lista = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Crianca::class.java)?.let { doc.id to it }
                    }
                    _perfis.value = lista
                    _isLoading.value = false
                }
            }
    }
}