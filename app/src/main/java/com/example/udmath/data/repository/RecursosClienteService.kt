package com.example.udmath.data.repository

import com.example.udmath.domain.model.Recurso
import com.example.udmath.domain.repository.RecursoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class RecursosClienteService @Inject constructor(
    private val firestore: FirebaseFirestore
) : RecursoRepository {


    override suspend fun getRecursosPorTipo(tipo: String): List<Recurso> {
        val snap = firestore.collection("recursos")
            .whereEqualTo("tipo", tipo)
            .get()
            .await()

        return snap.documents.map { d ->
            Recurso(
                titulo = d.getString("titulo").orEmpty(),
                tipo = d.getString("tipo").orEmpty(),
                modulo = d.getString("modulo").orEmpty(),
                fecha_agregado = d.getTimestamp("fecha_agregado"),
                descripcion = d.getString("descripcion").orEmpty(),
                autor = d.getString("autor").orEmpty()
            )
        }
    }
}