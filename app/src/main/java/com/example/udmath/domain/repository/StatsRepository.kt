package com.example.udmath.domain.repository
import com.example.udmath.domain.model.EncuestaResultado
import com.example.udmath.domain.model.MateriaDashboard
import com.example.udmath.domain.model.MateriaStats

interface StatsRepository {
    suspend fun submitEncuesta(uid: String, data: EncuestaResultado)

    suspend fun getDashboardBySemestres(
        semestres: List<String>
    ): List<MateriaDashboard>
}
