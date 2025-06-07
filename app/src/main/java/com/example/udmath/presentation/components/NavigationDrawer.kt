package com.example.udmath.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.udmath.R


@Composable
fun NavigationDrawer(){

    Column(modifier = Modifier.fillMaxWidth()) {

        Image(painter = painterResource(id = R.drawable.logo_ud),
            contentDescription = "Logo",
            modifier = Modifier.height(160.dp).
            fillMaxWidth(),
            contentScale = androidx.compose.ui.layout.ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(15.dp))

        TextButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Primera opción")
        }

    }

}