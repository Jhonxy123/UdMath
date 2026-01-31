package com.example.udmath.domain.repository

import com.example.udmath.domain.model.Recurso

interface RecursoRepository {

    suspend fun getRecursosPorTipo(tipo: String): List<Recurso>

}