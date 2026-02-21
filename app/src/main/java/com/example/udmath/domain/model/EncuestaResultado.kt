package com.example.udmath.domain.model
import com.google.firebase.Timestamp

enum class ResultadoMateria { APROBO, PERDIO }

data class EncuestaResultado(
    val semestre: String,
    val materiaId: String,
    val resultado: ResultadoMateria,
    val fecha: Timestamp = Timestamp.now()
)
