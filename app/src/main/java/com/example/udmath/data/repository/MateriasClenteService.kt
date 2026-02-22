package com.example.udmath.data.repository

import com.example.udmath.domain.model.Materia
import com.example.udmath.domain.repository.MateriaRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.udmath.domain.model.Pregunta
import com.example.udmath.domain.model.Progreso

class MateriasClenteService @Inject constructor(
    private val firestore: FirebaseFirestore
) : MateriaRepository {

    // Declaramos el metodo que traera los niveles de cada materia, recibiendo el nombre de la materia
    override suspend fun getTematicasByMateriaId(materiaId: String): List<Materia> {

        val snap = firestore.collection("materias")
            .document(materiaId)
            .collection("niveles")
            .orderBy("numero")
            .get()
            .await()

        return snap.documents.map { doc ->
            Materia(
                id = doc.id,
                numero = doc.getLong("numero")?.toInt() ?: 0,
                titulo = doc.getString("titulo").orEmpty(),
                totalpreguntas = doc.getLong("total_preguntas")?.toInt() ?: 0
            )
        }
    }



    override suspend fun getPreguntasByNivel(materiaId: String, nivelId: String): List<Pregunta> {
        val snap = firestore.collection("materias")
            .document(materiaId)
            .collection("niveles")
            .document(nivelId)
            .collection("preguntas")
            .get()
            .await()

        return snap.documents.map { doc ->
            Pregunta(
                id = doc.id,
                texto = doc.getString("texto").orEmpty(),
                opciones = doc.get("opciones_respuesta") as? List<String> ?: emptyList(),
                respuestaCorrecta = doc.getString("respuesta_correcta").orEmpty(),
                puntos = doc.getLong("puntos")?.toInt() ?: 0,
                tipo = doc.getString("tipo").orEmpty()
            )
        }
    }

    override suspend fun saveProgreso(
        uid: String,
        materiaId: String,
        nivelId: String,
        progreso: Progreso
    ) {
        val docId = "${materiaId}_${nivelId}"

        val data = hashMapOf(
            "fecha" to com.google.firebase.Timestamp.now(),
            "puntos_acumulados" to progreso.puntos,
            "respondidas" to progreso.respondidas,
            "total" to progreso.total,
            "respuestas" to progreso.respuestas,
            "materiaId" to materiaId,
            "nivelId" to nivelId,
            "materiaRef" to firestore.collection("materias").document(materiaId),
            "nivelRef" to firestore.collection("materias").document(materiaId)
                .collection("niveles").document(nivelId)
        )

        firestore.collection("usuarios")
            .document(uid)
            .collection("progreso")
            .document(docId)
            .set(data, com.google.firebase.firestore.SetOptions.merge())
            .await()
    }

    override suspend fun getProgreso(
        uid: String,
        materiaId: String,
        nivelId: String
    ): Progreso? {
        val docId = "${materiaId}_${nivelId}"

        val doc = firestore.collection("usuarios")
            .document(uid)
            .collection("progreso")
            .document(docId)
            .get()
            .await()

        if (!doc.exists()) return null

        val respuestas = doc.get("respuestas") as? Map<String, String> ?: emptyMap()

        return Progreso(
            puntos = doc.getLong("puntos_acumulados")?.toInt() ?: 0,
            respondidas = doc.getLong("respondidas")?.toInt() ?: 0,
            total = doc.getLong("total")?.toInt() ?: 0,
            respuestas = respuestas
        )
    }




}