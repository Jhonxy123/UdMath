package com.example.udmath.presentation.home


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.udmath.presentation.components.TopBar


@Composable
fun HomeScreen(viewModel: HomeViewModel){

    val drawerState = viewModel.drawerState.collectAsState()

    Column {
        TopBar()
    }


}

