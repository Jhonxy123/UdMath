import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.udmath.presentation.components.BubbleIcon
import com.example.udmath.presentation.navigation.HomeTab
import com.example.udmath.presentation.navigation.MaterialIntTab
import com.example.udmath.presentation.navigation.MaterialTab
import com.example.udmath.presentation.navigation.RecomendacionesTab
import com.example.udmath.presentation.navigation.RetosTab


data class BottomBarItem(
    val destination: Any,
    val label: String,
    val icon: ImageVector
)


@Composable
fun MainBottomBar(navController: NavHostController) {


    // Aquí se crean los sinco botones de bottom bar
    // Cada uno tiene el destino (pantalla a la que navega)
    // label, texto bajo el icono
    // icon, el icono
    // es decir, aquí se definen todos los tabs de la barra y foreach solo los meustra
    val items = listOf(
        BottomBarItem(HomeTab, "Inicio", Icons.Filled.Home),
        BottomBarItem(RecomendacionesTab, "Rec", Icons.Filled.Star),
        BottomBarItem(MaterialTab, "Material", Icons.Filled.MenuBook),
        BottomBarItem(MaterialIntTab, "Mat-Int", Icons.Filled.Lightbulb),
        BottomBarItem(RetosTab, "Retos", Icons.Filled.EmojiEvents)
    )


    //¿Cómo sabe cual tab esta seleccionado?
    // currentBackStackEntryAsState(). Observa la navegación en tiempo real
    // cada vez que cambia la pantalla esto se actualiza solo
    // como es state, Compose lo va a redibujar
    // De esta manera el bottom bar siempre sabe cuál pantalla está activa
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {

        items.forEach { item ->

            // CurrentRoute es la pantalla actual (string)
            // Item.route es la pantalla a la que queremos navegar (string)
            // En navegación tipada, la route suele ser el qualifiedName del tipo
            val itemRoute = item.destination::class.qualifiedName
            // si currentRoute y itemRoute son iguales, entonces el tab está seleccionado
            val selected = currentRoute == itemRoute


            // Esto es loq ue se dibuja en la pantalla
            NavigationBarItem(

                selected = selected, // si es True Compose lo cambia

                onClick = {

                    navController.navigate(item.destination) {

                        // Acá hacemos una navegación "Inteligente"
                        // popUpTo(startDestinationId) evita que se acumulen tabs cuando se cambia entre pantallas
                        popUpTo(navController.graph.startDestinationId) {
                            // Guarda el estado del tab (scroll, etc)
                            saveState = true
                        }
                        // Si el tab ya está seleccionado lo vuelve a seleccionar, es decir no crea una copia
                        launchSingleTop = true
                        // Restaura el estado del tab, es decir no vuelve a cargar desde cero
                        restoreState = true
                    }
                },
                // Icono del tab
                icon = {
                        BubbleIcon(
                            icon = item.icon,
                            contentDescription = item.label,
                            selected = selected
                        )
                },
                // Texto del tab
                label = { Text(item.label) }
            )
        }
    }
}
