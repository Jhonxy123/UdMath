package com.example.udmath.data.repository
import com.example.udmath.domain.model.EncuestaResultado
import com.example.udmath.domain.model.MateriaDashboard
import com.example.udmath.domain.model.MateriaStats
import com.example.udmath.domain.model.ResultadoMateria
import com.example.udmath.domain.repository.StatsRepository
import com.example.udmath.presentation.admin.Graficas.BarPoint
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StatsClientService @Inject constructor(
    private val firestore: FirebaseFirestore
) : StatsRepository {

    override suspend fun submitEncuesta(uid: String, data: EncuestaResultado) {
        validateSemestre(data.semestre)

        val semestre = data.semestre
        val materiaId = data.materiaId
        val resultadoNuevo = data.resultado.name // "APROBO" o "PERDIO"

        // Doc determinístico (si envía otra vez, actualiza el mismo doc)
        val encuestasDocId = "${semestre}_${materiaId}"
        val userEncuestaRef = firestore.collection("usuarios")
            .document(uid)
            .collection("encuestas")
            .document(encuestasDocId)

        // Agregado por semestre/materia (para admin)
        val statsRef = firestore.collection("estadisticas")
            .document(semestre)
            .collection("materias")
            .document(materiaId)

        firestore.runTransaction { tx ->
            val prevSnap = tx.get(userEncuestaRef)

            // 1) Si existía una respuesta anterior, restamos del contador anterior
            if (prevSnap.exists()) {
                val resultadoPrev = prevSnap.getString("resultado")
                if (resultadoPrev == ResultadoMateria.APROBO.name) {
                    tx.set(
                        statsRef,
                        mapOf(
                            "aprobados" to FieldValue.increment(-1),
                            "updatedAt" to Timestamp.now()
                        ),
                        com.google.firebase.firestore.SetOptions.merge()
                    )
                } else if (resultadoPrev == ResultadoMateria.PERDIO.name) {
                    tx.set(
                        statsRef,
                        mapOf(
                            "perdidos" to FieldValue.increment(-1),
                            "updatedAt" to Timestamp.now()
                        ),
                        com.google.firebase.firestore.SetOptions.merge()
                    )
                }
                // OJO: total no cambia cuando solo se modifica la respuesta.
            } else {
                // 2) Si NO existía, esta es una respuesta nueva -> total +1
                tx.set(
                    statsRef,
                    mapOf(
                        "total" to FieldValue.increment(1),
                        "updatedAt" to Timestamp.now()
                    ),
                    com.google.firebase.firestore.SetOptions.merge()
                )
            }

            // 3) Guardar/actualizar la respuesta del usuario (última respuesta)
            tx.set(
                userEncuestaRef,
                mapOf(
                    "semestre" to semestre,
                    "materiaId" to materiaId,
                    "resultado" to resultadoNuevo,
                    "fecha" to Timestamp.now()
                ),
                com.google.firebase.firestore.SetOptions.merge()
            )

            // 4) Sumar al contador NUEVO (aprobados o perdidos)
            when (data.resultado) {
                ResultadoMateria.APROBO -> tx.set(
                    statsRef,
                    mapOf(
                        "aprobados" to FieldValue.increment(1),
                        "updatedAt" to Timestamp.now()
                    ),
                    com.google.firebase.firestore.SetOptions.merge()
                )

                ResultadoMateria.PERDIO -> tx.set(
                    statsRef,
                    mapOf(
                        "perdidos" to FieldValue.increment(1),
                        "updatedAt" to Timestamp.now()
                    ),
                    com.google.firebase.firestore.SetOptions.merge()
                )
            }

            null
        }.await()
    }

    override suspend fun getDashboardBySemestres(
        semestres: List<String>
    ): List<MateriaDashboard> {

        val validSemestres = semestres
            .map { it.trim() }
            .filter { Regex("""^\d{4}-(1|3)$""").matches(it) }
            .sorted()

        // materiaId -> (semestre -> BarPoint)
        val map = linkedMapOf<String, MutableMap<String, BarPoint>>()

        for (sem in validSemestres) {
            val snap = firestore.collection("estadisticas")
                .document(sem)
                .collection("materias")
                .get()
                .await()

            for (doc in snap.documents) {
                val materiaId = doc.id
                val aprobados = doc.getLong("aprobados")?.toInt() ?: 0
                val perdidos = doc.getLong("perdidos")?.toInt() ?: 0

                val point = BarPoint(
                    label = sem,
                    aprobado = aprobados,
                    desaprobado = perdidos
                )

                val perMateria = map.getOrPut(materiaId) { linkedMapOf() }
                perMateria[sem] = point
            }
        }

        // Rellena semestres faltantes con 0 para cada materia (para que el eje X sea consistente)
        return map.map { (materiaId, perSem) ->
            val points = validSemestres.map { sem ->
                perSem[sem] ?: BarPoint(label = sem, aprobado = 0, desaprobado = 0)
            }
            MateriaDashboard(materiaId = materiaId, puntos = points)
        }.sortedBy { it.materiaId }
    }


    private fun validateSemestre(semestre: String) {
        // Acepta solo YYYY-1 o YYYY-3
        val ok = Regex("""^\d{4}-(1|3)$""").matches(semestre)
        if (!ok) {
            throw IllegalArgumentException("Semestre inválido. Usa YYYY-1 o YYYY-3 (no se cuenta -2).")
        }
    }
}
