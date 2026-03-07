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
                id = d.id,
                titulo = d.getString("titulo").orEmpty(),
                tipo = d.getString("tipo").orEmpty(),
                modulo = d.getString("modulo").orEmpty(),
                fecha_agregado = d.getTimestamp("fecha_agregado"),
                descripcion = d.getString("descripcion").orEmpty(),
                autor = d.getString("autor").orEmpty(),
                imagen = d.getString("image").orEmpty(),
                url = d.getString("url").orEmpty(),
                autorId = d.getString("autorId").orEmpty()
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
            "autor" to recurso.autor,
            "fecha_agregado" to com.google.firebase.Timestamp.now()
        )

        firestore.collection("recursos")
            .add(data)
            .await()
    }

    override suspend fun getRecursosPorModulo(modulo: String): List<Recurso> {
        return firestore.collection("recursos")
            .whereEqualTo("modulo", modulo)
            .get()
            .await()
            .documents
            .mapNotNull { d ->
                Recurso(
                    id = d.id,
                    titulo = d.getString("titulo").orEmpty(),
                    descripcion = d.getString("descripcion").orEmpty(),
                    url = d.getString("url").orEmpty(),
                    imagen = d.getString("image").orEmpty(),
                    autor = d.getString("autor").orEmpty(),
                    tipo = d.getString("tipo").orEmpty(),
                    modulo = d.getString("modulo").orEmpty(),
                    autorId = d.getString("autorId").orEmpty(),
                    fecha_agregado = d.getTimestamp("fecha_agregado")
                )
            }
    }

    override suspend fun eliminarRecurso(id: String) {
        firestore.collection("recursos")
            .document(id)
            .delete()
            .await()
    }

    override suspend fun editarRecurso(recurso: Recurso) {
        val data = hashMapOf(
            "titulo" to recurso.titulo,
            "tipo" to recurso.tipo,
            "modulo" to recurso.modulo,
            "descripcion" to recurso.descripcion,
            "url" to recurso.url,
            "image" to recurso.imagen,
            "autorId" to recurso.autorId,
            "autor" to recurso.autor
        )

        firestore.collection("recursos")
            .document(recurso.id)
            .update(data as Map<String, Any>)
            .await()
    }
}