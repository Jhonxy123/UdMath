package com.example.udmath.presentation.Recomendaciones.Components

import androidx.lifecycle.ViewModel
import com.example.udmath.domain.repository.MateriaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject





@HiltViewModel
class MateriaViewModel @Inject constructor(
    private val repo: MateriaRepository
) : ViewModel() {



}