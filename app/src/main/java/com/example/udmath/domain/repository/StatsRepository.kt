package com.example.udmath.domain.repository
import com.example.udmath.domain.model.EncuestaResultado

interface StatsRepository {
    suspend fun submitEncuesta(uid: String, data: EncuestaResultado)
}
