package com.example.udmath.data.repository
import com.example.udmath.domain.model.EncuestaResultado
import com.example.udmath.domain.model.ResultadoMateria
import com.example.udmath.domain.repository.StatsRepository
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

    private fun validateSemestre(semestre: String) {
        // Acepta solo YYYY-1 o YYYY-3
        val ok = Regex("""^\d{4}-(1|3)$""").matches(semestre)
        if (!ok) {
            throw IllegalArgumentException("Semestre inválido. Usa YYYY-1 o YYYY-3 (no se cuenta -2).")
        }
    }
}
