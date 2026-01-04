package com.example.udmath.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.udmath.presentation.components.NavigationDrawer
import com.example.udmath.presentation.components.TopBar
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.udmath.R
import com.example.udmath.ui.theme.white
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLoggedOut: () -> Unit = {},   // navController lo pasará; para Preview queda vacío
    navigateToProfile: () -> Unit = {}
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val userState = viewModel.user.collectAsState()


    //Definimos el gradiente de colores
    val blueGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3980C2),
            Color(0xFF184998)
        )
    )

    // Escuchamos eventos del ViewModel (por ejemplo, LoggedOut)
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                HomeUiEvent.LoggedOut -> onLoggedOut()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawer(
                    user = userState.value,
                    // 1. Cerramos el drawer
                    // 2. Avisamos al ViewModel que se pidió logout
                    onLogout = {
                        // 1. Cerramos el drawer
                        scope.launch { drawerState.close() }
                        // 2. Avisamos al ViewModel que se pidió logout
                        viewModel.onLogoutClicked()
                    },

                    onProfileClicked = {
                        scope.launch { drawerState.close() }
                        navigateToProfile()
                    }


                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .background(blueGradient)
                .fillMaxSize()
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopBar(
                        onDrawerClicked = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        text = "Inicio"
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        )

                        Image(
                            painter = painterResource(id = R.drawable.logo_inicio),
                            contentDescription = "Logo",
                            Modifier.size(400.dp)
                        )

                        Text(
                            modifier = Modifier.padding(start = 20.dp),
                            textAlign = TextAlign.Left,
                            text = "UdMath",
                            fontWeight = FontWeight.Bold,
                            color = white
                        )

                        Text(
                            modifier = Modifier.padding(start = 20.dp),
                            text = "Bienvenido a UdMath usuario, esta aplicación ha sido diseñada para ayudarte en " +
                                    "el recorrido de tus materias de ciencias basicas, " +
                                    "para comenzar selecciona en la parte inferior la sección a la que desea ingresar. ",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

