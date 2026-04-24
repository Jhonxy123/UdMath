package com.example.udmath.presentation.admin

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.ui.theme.white

@Composable
fun AdminScreen(
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    val filtered = remember(state.users, state.search) {
        val q = state.search.trim().lowercase()
        if (q.isEmpty()) state.users
        else state.users.filter {
            it.name.lowercase().contains(q) || it.email.lowercase().contains(q)
        }
    }

    Scaffold(
        topBar = {
            AdminTopBar(
                onDrawerClicked = { },
                text = "Inicio",
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                FloatingAnimatedBottomBar(
                    selectedIndex = selectedTab,
                    onSelect = { selectedTab = it }
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F3F3))
                .padding(innerPadding)
                .imePadding()
        ) {
            Spacer(Modifier.height(14.dp))

            SearchBox(
                value = state.search,
                onValueChange = viewModel::onSearchChange,
                modifier = Modifier.padding(horizontal = 18.dp)
            )

            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                UsersTableCard(
                    isLoading = state.isLoading,
                    error = state.error,
                    users = filtered,
                    onRefresh = { viewModel.loadUsers() }
                )
            }
        }
    }
}

@Composable
fun AdminTopBar(
    onDrawerClicked: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = white),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Left,
            color = Color(0xFF184998),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 20.dp)
        )

        IconButton(
            onClick = onDrawerClicked,
            modifier = Modifier.size(70.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color(0xFF184998),
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
private fun SearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = { Text("Buscar Datos") },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp)),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFEDEDED),
            unfocusedContainerColor = Color(0xFFEDEDED),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Color(0xFF184998)
        )
    )
}

@Composable
private fun UsersTableCard(
    isLoading: Boolean,
    error: String?,
    users: List<AdminUserRow>,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Usuario",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF184998),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Correo",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF184998),
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(70.dp))
            }

            Divider(color = Color(0xFF184998), thickness = 1.dp)

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    error != null -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(error, color = Color.Red, fontSize = 13.sp)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = onRefresh) {
                                Text("Reintentar")
                            }
                        }
                    }

                    users.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tabla vacía (sin usuarios)", color = Color.Gray)
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(users) { user ->
                                UserRow(user)
                                Divider(color = Color(0xFFE6E6E6))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserRow(user: AdminUserRow) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.name.ifBlank { "(sin nombre)" },
            modifier = Modifier.weight(1f),
            color = Color(0xFF184998),
            fontSize = 16.sp
        )

        Text(
            text = user.email.ifBlank { "(sin correo)" },
            modifier = Modifier.weight(1f),
            color = Color(0xFF184998),
            fontSize = 16.sp
        )

        Row(
            modifier = Modifier.width(70.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Eliminar",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE53935))
                    .padding(6.dp)
            )

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E7D32))
                    .padding(6.dp)
            )
        }
    }
}

@Composable
private fun FloatingAnimatedBottomBar(
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    val items = listOf(
        Icons.Default.Home,
        Icons.Default.List,
        Icons.Default.Edit
    )

    val barHeight = 66.dp
    val paddingH = 22.dp
    val indicatorSize = 44.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = paddingH)
                .height(barHeight)
                .shadow(18.dp, RoundedCornerShape(30.dp))
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, icon ->
                    IconButton(
                        onClick = { onSelect(index) },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (index == selectedIndex) Color(0xFF184998) else Color(0xFF9E9E9E),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            BoxWithConstraints(Modifier.fillMaxSize()) {
                val totalW = maxWidth
                val slot = totalW / items.size
                val target = slot * selectedIndex + (slot - indicatorSize) / 2

                val animatedX by animateDpAsState(
                    targetValue = target,
                    label = "indicatorX"
                )

                Box(
                    modifier = Modifier
                        .offset(x = animatedX, y = (barHeight - indicatorSize) / 2)
                        .size(indicatorSize)
                        .clip(CircleShape)
                        .border(1.dp, Color(0xFFE6E6E6), CircleShape)
                        .background(Color(0xFFF6F6F6))
                )
            }
        }
    }
}