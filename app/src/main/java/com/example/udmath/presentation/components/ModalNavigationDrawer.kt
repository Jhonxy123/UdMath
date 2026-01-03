package com.example.udmath.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.R
import com.example.udmath.domain.model.UserUi
import com.example.udmath.presentation.home.HomeViewModel
import com.example.udmath.ui.theme.white
import kotlinx.coroutines.launch


@Composable
fun Modal(
    drawerState: DrawerState,
    userState: UserUi?,
    viewModel: HomeViewModel = hiltViewModel(),
    onLoggedOut: () -> Unit = {},
    content: @Composable () -> Unit
){

    val drawerState = drawerState
    val scope = rememberCoroutineScope()

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
                        }
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