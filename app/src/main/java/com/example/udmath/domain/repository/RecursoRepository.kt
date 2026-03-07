package com.example.udmath.domain.repository

import com.example.udmath.domain.model.Recurso

interface RecursoRepository {

    suspend fun getRecursosPorTipo(tipo: String): List<Recurso>

    suspend fun crearRecurso(recurso: Recurso)

    suspend fun getRecursosPorModulo(modulo: String): List<Recurso>

    suspend fun eliminarRecurso(id: String)

    suspend fun editarRecurso(recurso: Recurso)

    suspend fun getRecursoById(id: String): Recurso?

}