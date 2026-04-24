package com.example.udmath.presentation.admin.BaseDatos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.presentation.admin.AdminUserRow

@Composable
fun BdScreen(
    viewModel: BdViewModel = hiltViewModel(),
    navigateToEdit: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    var userToDelete by remember { mutableStateOf<AdminUserRow?>(null) }

    val filtered = remember(state.users, state.search) {
        val q = state.search.trim().lowercase()
        if (q.isEmpty()) state.users
        else state.users.filter {
            it.name.lowercase().contains(q) || it.email.lowercase().contains(q)
        }
    }

    if (userToDelete != null) {
        AlertDialog(
            onDismissRequest = { userToDelete = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de eliminar este usuario?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val id = userToDelete!!.id
                        userToDelete = null
                        viewModel.deleteUser(id)
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { userToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
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
                onRefresh = { viewModel.loadUsers() },
                onDeleteClicked = { user -> userToDelete = user },
                onEditClicked = { user -> navigateToEdit(user.id) }
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
    onRefresh: () -> Unit,
    onDeleteClicked: (AdminUserRow) -> Unit,
    onEditClicked: (AdminUserRow) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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

                Spacer(Modifier.width(56.dp))
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
                                UserRow(
                                    user = user,
                                    onDelete = { onDeleteClicked(user) },
                                    onEdit = { onEditClicked(user) }
                                )

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
private fun UserRow(
    user: AdminUserRow,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.name.ifBlank { "(sin nombre)" },
            modifier = Modifier.weight(1f),
            color = Color(0xFF184998)
        )

        Text(
            text = user.email.ifBlank { "(sin correo)" },
            modifier = Modifier.weight(1f),
            color = Color(0xFF184998)
        )

        Row(
            modifier = Modifier.width(56.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleActionButton(
                color = Color(0xFFE53935),
                icon = Icons.Default.Delete,
                onClick = onDelete
            )

            CircleActionButton(
                color = Color(0xFF2E7D32),
                icon = Icons.Default.Edit,
                onClick = onEdit
            )
        }
    }
}

@Composable
fun CircleActionButton(
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(12.dp)
        )
    }
}