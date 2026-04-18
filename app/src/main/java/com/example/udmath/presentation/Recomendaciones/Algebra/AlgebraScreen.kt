package com.example.udmath.presentation.Recomendaciones.Algebra

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.udmath.presentation.Recomendaciones.Components.MateriasScreen
import com.example.udmath.presentation.components.TopBarback

@Composable
fun AlgebraScreen(
    navigateBack: () -> Unit,
    navigateToPreguntas: (materiaId: String, nivelId: String) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBarback("Algebra", navigateBack = { navigateBack() }) }
    ) { padding ->
        MateriasScreen(
            materia = "algebra",
            modifier = Modifier.padding(padding),
            onclickNivel = { nivelId ->
                navigateToPreguntas("algebra", nivelId)
            },
            onAbrirFormulario = {
                val formsUrl = "https://forms.office.com/Pages/ResponsePage.aspx?id=74gT1bBqY0OflNVmRKRZcE8qQbJiOsxDmBdiRfan48NUQzRYSkRQNk1ZTDVKVzU5ODhWRVlORjdSUS4u"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formsUrl))
                context.startActivity(intent)
            }
        )
    }
}



