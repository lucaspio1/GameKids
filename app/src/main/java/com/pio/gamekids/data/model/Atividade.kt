package com.pio.gamekids.data.model

import com.google.firebase.Timestamp

data class Atividade(
    val id: String = "",
    val titulo: String = "",
    val valor: Int = 0,
    val status: String = "pendente",
    val foto_prova_url: String = "",
    val data_limite: Timestamp? = null
)