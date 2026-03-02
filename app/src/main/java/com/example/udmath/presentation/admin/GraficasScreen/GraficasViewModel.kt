package com.example.udmath.presentation.admin.GraficasScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.repository.StatsRepository
import com.example.udmath.presentation.admin.Graficas.BarPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import com.example.udmath.domain.model.MateriaDashboard

@HiltViewModel
class GraficasViewModel @Inject constructor(
    private val repo: StatsRepository
) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val error: String? = null,
        val dashboards: List<MateriaDashboard> = emptyList()
    )

    private val _state = androidx.compose.runtime.mutableStateOf(UiState())
    val state: androidx.compose.runtime.State<UiState> = _state

    fun loadDashboard(semestres: List<String>) {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            try {
                val data = repo.getDashboardBySemestres(semestres)
                _state.value = UiState(dashboards = data)
            } catch (e: Exception) {
                _state.value = UiState(error = e.message ?: "Error cargando gráficas")
            }
        }
    }
}