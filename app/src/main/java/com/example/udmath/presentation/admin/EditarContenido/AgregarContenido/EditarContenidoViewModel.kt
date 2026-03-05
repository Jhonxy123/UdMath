package com.example.udmath.presentation.admin.EditarContenido.AgregarContenido

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.Recurso
import com.example.udmath.domain.repository.RecursoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State

data class AdminCrearRecursoUiState(
    val titulo: String = "",
    val tipo: String = "",
    val modulo: String = "",
    val descripcion: String = "",
    val url: String = "",
    val image: String = "",
    val isSaving: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AdminCrearRecursoViewModel @Inject constructor(
    private val repo: RecursoRepository
) : ViewModel() {

    private val _state = mutableStateOf(AdminCrearRecursoUiState())
    val state: State<AdminCrearRecursoUiState> = _state

    fun onTituloChange(v: String) = update { copy(titulo = v, successMessage = null, errorMessage = null) }
    fun onTipoChange(v: String) = update { copy(tipo = v, successMessage = null, errorMessage = null) }
    fun onModuloChange(v: String) = update { copy(modulo = v, successMessage = null, errorMessage = null) }
    fun onDescripcionChange(v: String) = update { copy(descripcion = v, successMessage = null, errorMessage = null) }
    fun onUrlChange(v: String) = update { copy(url = v, successMessage = null, errorMessage = null) }
    fun onImageChange(v: String) = update { copy(image = v, successMessage = null, errorMessage = null) }

    private inline fun update(block: AdminCrearRecursoUiState.() -> AdminCrearRecursoUiState) {
        _state.value = _state.value.block()
    }

    fun guardarRecurso(autorId: String, autorNombre: String) {
        val s = _state.value

        // Validaciones mínimas
        if (s.titulo.isBlank() || s.tipo.isBlank() || s.modulo.isBlank() || s.url.isBlank()) {
            _state.value = s.copy(errorMessage = "Completa título, tipo, módulo y URL.")
            return
        }

        viewModelScope.launch {
            _state.value = s.copy(isSaving = true, errorMessage = null, successMessage = null)

            runCatching {
                repo.crearRecurso(
                    Recurso(
                        titulo = s.titulo.trim(),
                        tipo = s.tipo.trim(),
                        modulo = s.modulo.trim(),
                        descripcion = s.descripcion.trim(),
                        url = s.url.trim(),
                        imagen = s.image.trim(),
                        autorId = autorId,
                        autor = autorNombre
                    )
                )
            }.onSuccess {
                _state.value = AdminCrearRecursoUiState(
                    successMessage = "Recurso agregado correctamente ✅"
                )
            }.onFailure { e ->
                _state.value = s.copy(isSaving = false, errorMessage = e.message ?: "Error guardando recurso.")
            }
        }
    }
}