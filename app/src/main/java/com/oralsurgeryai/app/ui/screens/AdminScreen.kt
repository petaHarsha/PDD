package com.oralsurgeryai.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oralsurgeryai.app.data.NetworkModule
import com.oralsurgeryai.app.ui.theme.Secondary
import kotlinx.coroutines.launch

@Composable
fun AdminScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var users by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var auditLogs by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var metrics by remember { mutableStateOf<Map<String, Any>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableIntStateOf(0) }

    // Security Gate: Confirmation State
    var userToPurge by remember { mutableStateOf<Int?>(null) }

    fun loadData() {
        scope.launch {
            try {
                isLoading = true
                val usersResponse = NetworkModule.apiService.getUsers()
                users = usersResponse
                
                val metricsResponse = NetworkModule.apiService.getSystemMetrics()
                metrics = metricsResponse

                val logsResponse = NetworkModule.apiService.getAuditLogs()
                auditLogs = logsResponse
            } catch (e: Exception) {
                Toast.makeText(context, "Admin Sync Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadData()
    }

    // PURGE CONFIRMATION DIALOG
    userToPurge?.let { userId ->
        AlertDialog(
            onDismissRequest = { userToPurge = null },
            title = { Text("Confirm Staff Removal", fontWeight = FontWeight.Bold) },
            text = { Text("This action will permanently remove this clinician and all associated logs from the local directory. This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                NetworkModule.apiService.deleteUser(userId)
                                loadData()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Purge Failed", Toast.LENGTH_SHORT).show()
                            }
                            userToPurge = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Purge Account") }
            },
            dismissButton = {
                TextButton(onClick = { userToPurge = null }) { Text("Cancel") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Tab Row for Admin Features
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Secondary
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("STAFF", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("AUDIT", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("SYSTEM", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Secondary)
            }
        } else {
            when (selectedTab) {
                0 -> StaffList(users, onUpdate = { loadData() }, onPurgeRequest = { userToPurge = it })
                1 -> AuditTrail(auditLogs)
                2 -> SystemHealth(metrics)
            }
        }
    }
}

@Composable
fun StaffList(users: List<Map<String, Any>>, onUpdate: () -> Unit, onPurgeRequest: (Int) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(users) { user ->
            val id = (user["id"] as? Double)?.toInt() ?: 0
            val isActive = (user["is_active"] as? Double)?.toInt() == 1
            
            UserCard(
                id = id,
                email = user["email"] as? String ?: "",
                name = user["full_name"] as? String ?: "",
                role = user["role"] as? String ?: "",
                isActive = isActive,
                caseCount = (user["case_count"] as? Double)?.toInt() ?: 0,
                onPromote = { newRole ->
                    scope.launch {
                        try {
                            NetworkModule.apiService.promoteUser(id, newRole)
                            onUpdate()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Promotion Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onToggleActive = {
                    scope.launch {
                        try {
                            NetworkModule.apiService.toggleUserActive(id)
                            onUpdate()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Security Toggle Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onDelete = { onPurgeRequest(id) }
            )
        }
    }
}

@Composable
fun AuditTrail(logs: List<Map<String, Any>>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(logs) { log ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.LightGray)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    val action = log["action"] as? String ?: ""
                    Icon(
                        imageVector = if (action == "Login") Icons.Default.LockOpen else Icons.Default.Shield,
                        contentDescription = null,
                        tint = if (action == "Login") Color.Green else Secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(action, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        Text(log["user_email"] as? String ?: "", fontSize = 10.sp, color = Color.Gray)
                        Text(log["details"] as? String ?: "", fontSize = 10.sp, color = Color.LightGray)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = (log["timestamp"] as? String ?: "").take(10),
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun SystemHealth(metrics: Map<String, Any>) {
    Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
        Text("INFRASTRUCTURE STATUS", style = MaterialTheme.typography.labelLarge, color = Secondary)
        Spacer(modifier = Modifier.height(16.dp))
        
        MetricRow("API Status", metrics["api_health"] as? String ?: "Offline", Icons.Default.Wifi)
        MetricRow("Storage Used", metrics["storage_used"] as? String ?: "0 GB", Icons.Default.Storage)
        MetricRow("Uptime", metrics["server_uptime"] as? String ?: "Unknown", Icons.Default.Timer)
        MetricRow("Total Clinical Cases", (metrics["total_patients"] as? Double)?.toInt()?.toString() ?: "0", Icons.Default.FilePresent)
    }
}

@Composable
fun MetricRow(label: String, value: String, icon: ImageVector) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Secondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, modifier = Modifier.weight(1f), fontSize = 14.sp)
            Text(value, fontWeight = FontWeight.Black, color = Secondary)
        }
    }
}

@Composable
fun UserCard(
    id: Int, 
    email: String, 
    name: String, 
    role: String, 
    isActive: Boolean,
    caseCount: Int,
    onPromote: (String) -> Unit,
    onToggleActive: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (isActive) Color.White else Color(0xFFFFF1F1)),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isActive) Color.Transparent else Color.Red.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(email, fontSize = 12.sp, color = Color.Gray)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                        Surface(
                            color = Secondary.copy(alpha = 0.1f),
                            shape = CircleShape
                        ) {
                            Text(
                                text = role.uppercase(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Secondary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$caseCount Cases", fontSize = 10.sp, color = Color.Gray)
                    }
                }
                
                Switch(
                    checked = isActive, 
                    onCheckedChange = { onToggleActive() },
                    colors = SwitchDefaults.colors(checkedThumbColor = Secondary)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onPromote(if (role == "User") "Surgeon" else "User") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (role == "User") "Promote" else "Demote", fontSize = 10.sp)
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.background(Color(0xFFFFEDED), RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.DeleteForever, null, tint = Color.Red, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
