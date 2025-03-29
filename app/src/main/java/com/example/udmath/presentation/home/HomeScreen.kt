package com.example.udmath.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.udmath.presentation.components.NavigationDrawer
import com.example.udmath.presentation.components.TopBar
import kotlinx.coroutines.launch


@Composable
fun HomeScreen() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawer()
            }
        }
    ) {
        // Contenido de la pantalla principal
        Scaffold(
            topBar = {
                TopBar(
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        ) { paddingValues ->  // Este es el parámetro content que faltaba
            // Aquí va el contenido principal de tu pantalla
            Box(modifier = Modifier.padding(paddingValues)) {
                // Tus componentes van aquí
                Text("Contenido principal")
            }
        }
    }
}

