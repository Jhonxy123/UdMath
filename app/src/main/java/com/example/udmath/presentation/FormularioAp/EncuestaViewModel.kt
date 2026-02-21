package com.example.udmath.presentation.FormularioAp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.EncuestaResultado
import com.example.udmath.domain.model.ResultadoMateria
import com.example.udmath.domain.repository.StatsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EncuestaViewModel @Inject constructor(
    private val statsRepo: StatsRepository
) : ViewModel() {

    fun enviar(semestre: String, materiaId: String, aprobo: Boolean) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val data = EncuestaResultado(
            semestre = semestre,
            materiaId = materiaId,
            resultado = if (aprobo) ResultadoMateria.APROBO else ResultadoMateria.PERDIO
        )

        viewModelScope.launch {
            statsRepo.submitEncuesta(uid, data)
        }
    }
}
