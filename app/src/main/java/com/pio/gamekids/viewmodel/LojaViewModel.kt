package com.pio.gamekids.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pio.gamekids.data.model.Recompensa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LojaViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val familiaId = "id_familia_exemplo"

    private val _recompensas = MutableStateFlow<List<Recompensa>>(emptyList())
    val recompensas: StateFlow<List<Recompensa>> = _recompensas

    init {
        carregarLoja()
    }

    private fun carregarLoja() {
        // A loja é da família, todos veem os mesmos prêmios
        db.collection("usuarios").document(familiaId).collection("loja")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val lista = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Recompensa::class.java)?.copy(id = doc.id)
                    }
                    _recompensas.value = lista
                }
            }
    }

    fun comprarItem(context: Context, criancaId: String, saldoAtual: Int, recompensa: Recompensa) {
        if (saldoAtual < recompensa.custo) {
            Toast.makeText(context, "Puxa! Faltam ${recompensa.custo - saldoAtual} moedas. Faça mais missões!", Toast.LENGTH_LONG).show()
            return
        }

        val novoSaldo = saldoAtual - recompensa.custo
        val criancaRef = db.collection("usuarios").document(familiaId).collection("criancas").document(criancaId)

        // Atualiza o saldo no banco de dados
        criancaRef.update("saldo_atual", novoSaldo)
            .addOnSuccessListener {
                Toast.makeText(context, "Parabéns! Você comprou: ${recompensa.titulo} 🎉", Toast.LENGTH_LONG).show()
                // Aqui no futuro podemos salvar num histórico de "pedidos" para os pais aprovarem/entregarem
            }
            .addOnFailureListener {
                Toast.makeText(context, "Erro na compra, tente novamente.", Toast.LENGTH_SHORT).show()
            }
    }
}