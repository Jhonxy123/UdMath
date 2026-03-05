package com.example.udmath.presentation.Recomendaciones.Components.Tablas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.data.repository.MateriasClenteService // ajusta el package
import com.example.udmath.domain.model.Progreso               // ajusta el package
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

private const val MINI_JUEGO_WIN = "__WIN__"

@HiltViewModel
class OnJuegoTerminadoViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val materiasClienteService: MateriasClenteService
) : ViewModel() {

}