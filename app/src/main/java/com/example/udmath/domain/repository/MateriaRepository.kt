package com.example.udmath.domain.repository


import com.example.udmath.domain.model.Materia
import com.example.udmath.domain.model.Pregunta
import com.example.udmath.domain.model.Progreso
import com.example.udmath.domain.model.Recurso

interface MateriaRepository {



    suspend fun getTematicasByMateriaId(materiaId: String): List<Materia>

    suspend fun getPreguntasByNivel(materiaId: String, nivelId: String): List<Pregunta>

    suspend fun saveProgreso (uid: String, materiaId: String, nivelId: String, progreso: Progreso)

    suspend fun getProgreso(uid: String, materiaId: String, nivelId: String): Progreso?



} 