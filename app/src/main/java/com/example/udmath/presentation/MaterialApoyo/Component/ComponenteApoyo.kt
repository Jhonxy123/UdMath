import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import com.example.udmath.presentation.MaterialApoyo.Component.ListaRecursosScreen
import com.example.udmath.presentation.MaterialApoyo.Component.RecursosViewModel

@Composable
fun RecursosScreen(
    tipo: String,               // "Libro", "App", "Artículo", "Video"
    navigateBack: () -> Unit,
    viewModel: RecursosViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    LaunchedEffect(tipo) {
        viewModel.load(tipo)
    }

    when {
        state.loading -> Text("Cargando...", color = Color.White)
        state.error != null -> Text("Error: ${state.error}", color = Color.White)
        else -> ListaRecursosScreen(
            tituloTopBar = tipo,
            recursos = state.recursos,
            navigateBack = navigateBack
        )
    }
}
