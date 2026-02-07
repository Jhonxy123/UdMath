package com.example.udmath.domain.repository

import com.example.udmath.domain.model.Materia

interface MateriaRepository {



    suspend fun getTematicasByMateriaId(materiaId: String): List<Materia>

}