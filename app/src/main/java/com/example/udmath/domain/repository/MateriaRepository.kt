package com.example.udmath.domain.repository


import com.example.udmath.domain.model.Materia
import com.example.udmath.domain.model.Pregunta

interface MateriaRepository {



    suspend fun getTematicasByMateriaId(materiaId: String): List<Materia>

    suspend fun getPreguntasByNivel(materiaId: String, nivelId: String): List<Pregunta>

}