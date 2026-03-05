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
                autor = d.getString("autor").orEmpty(),
                imagen = d.getString("image").orEmpty(),
                url = d.getString("url").orEmpty()
            )
        }
    }


    override suspend fun crearRecurso(recurso: Recurso) {
        val data = hashMapOf(
            "titulo" to recurso.titulo,
            "tipo" to recurso.tipo,
            "modulo" to recurso.modulo,
            "descripcion" to recurso.descripcion,
            "url" to recurso.url,
            "image" to recurso.imagen,
            "autorId" to recurso.autorId,
            "autorNombre" to recurso.autor,
            "fecha_agregado" to com.google.firebase.Timestamp.now()
        )
        firestore.collection("recursos")
            .add(data) // id automático está bien para recursos
            .await()
    }


}