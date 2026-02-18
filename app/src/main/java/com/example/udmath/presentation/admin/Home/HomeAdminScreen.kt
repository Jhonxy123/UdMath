package com.example.udmath.presentation.admin.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.R
import com.example.udmath.presentation.admin.AdminViewModel
import com.example.udmath.presentation.components.NavigationDrawer
import com.example.udmath.presentation.components.TopBar
import com.example.udmath.presentation.home.HomeUiEvent
import com.example.udmath.ui.theme.white
import kotlinx.coroutines.launch

@Composable
fun HomeAdminScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onLoggedOut: () -> Unit = {},
    navigateToProfile: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val userState = viewModel.user.collectAsState()

    val adminBackground = Color(0xFFECECEC)
    val adminTextColor = Color(0xFF27496E)

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
                    onLogout = {
                        scope.launch { drawerState.close() }
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
                .background(adminBackground)
                .fillMaxSize()
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopBar(
                        onDrawerClicked = { scope.launch { drawerState.open() } },
                        text = "Inicio"
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // ✅ Imagen centrada
                    Image(
                        painter = painterResource(id = R.drawable.inicio_admin),
                        contentDescription = "Inicio Admin",
                        modifier = Modifier.size(280.dp)
                    )

                    // ✅ Textos alineados a la izquierda
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, top = 8.dp),
                        textAlign = TextAlign.Left,
                        text = "UdMath",
                        fontWeight = FontWeight.Bold,
                        color = adminTextColor
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, top = 4.dp, end = 20.dp),
                        text = "Bienvenido administrador, esta aplicación ha sido diseñada para gestionar el contenido y los usuarios del sistema.",
                        color = adminTextColor
                    )
                }
            }
        }
    }
}
