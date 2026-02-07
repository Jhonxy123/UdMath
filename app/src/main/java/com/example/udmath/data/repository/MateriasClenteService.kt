package com.example.udmath.data.repository

import com.example.udmath.domain.model.Materia
import com.example.udmath.domain.repository.MateriaRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MateriasClenteService @Inject constructor(
    private val firestore: FirebaseFirestore
) : MateriaRepository {

    override suspend fun getTematicasByMateriaId(materiaId: String): List<Materia> {

        val snap = firestore.collection("materias")
            .document(materiaId)
            .collection("niveles")
            .orderBy("numero")
            .get()
            .await()

        return snap.documents.map { doc ->
            Materia(
                numero = doc.getLong("numero")?.toInt() ?: 0,
                titulo = doc.getString("titulo").orEmpty(),
                totalpreguntas = doc.getLong("total_preguntas")?.toInt() ?: 0
            )
        }
    }
}