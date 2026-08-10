package com.oralsurgeryai.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oralsurgeryai.app.data.ClinicalNote
import com.oralsurgeryai.app.data.Patient
import com.oralsurgeryai.app.data.UserSession
import com.oralsurgeryai.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onViewCbct: () -> Unit,
    onViewPrognosis: () -> Unit,
    onViewTraining: () -> Unit,
    onViewAdmin: () -> Unit,
    onViewNerve: () -> Unit,
    onViewTumor: () -> Unit
) {
    val userRole = UserSession.userRole
    val isUser = userRole == "User"
    val isSurgeon = userRole == "Surgeon"
    
    val scope = rememberCoroutineScope()
    val apiService = com.oralsurgeryai.app.data.NetworkModule.apiService
    
    var searchQuery by remember { mutableStateOf(UserSession.patientId ?: "") }
    var patients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    var patientNotes by remember { mutableStateOf<List<ClinicalNote>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var userStatusLabel by remember { mutableStateOf("Verified Patient") }
    
    // New Patient Form State
    var isAddModalOpen by remember { mutableStateOf(false) }
    var newPatientName by remember { mutableStateOf("") }
    var newPatientAge by remember { mutableStateOf("") }
    var newPatientIdToLink by remember { mutableStateOf("") }
    var isAdding by remember { mutableStateOf(false) }

    // Load Data
    LaunchedEffect(Unit) {
        loading = true
        try {
            if (isSurgeon || userRole == "Admin") {
                patients = apiService.getPatients()
            } else if (isUser && searchQuery.isNotEmpty()) {
                patientNotes = apiService.getPatientNotes(searchQuery)
                val statusRes = apiService.getUserPatientStatus(searchQuery)
                userStatusLabel = statusRes["status"] ?: "Verified Patient"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            loading = false
        }
    }

    val filteredPatients = patients.filter { it.name.contains(searchQuery, ignoreCase = true) || it.id.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Welcome Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Welcome, ${if (isSurgeon) "Dr. " else ""}${UserSession.userName}",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 24.sp,
                    lineHeight = 32.sp
                )
                Text(
                    if (isUser) "Access your clinical records and AI reports." else "Tuesday, Oct 24 • Clinical Review Mode",
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariant,
                    fontSize = 14.sp
                )
                if (isUser) {
                    Text(
                        userStatusLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (userStatusLabel == "Active Patient") Primary else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            if (isSurgeon) {
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { isAddModalOpen = true },
                    color = Primary,
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PersonAdd, null, tint = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (!isUser) {
            // KPI Grid for Surgeons/Admins
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                KPICard(
                    title = "DIAGNOSTIC ACCURACY",
                    value = "76.0%",
                    icon = Icons.Default.Verified,
                    color = Secondary,
                    modifier = Modifier.weight(1f)
                )
                KPICard(
                    title = "ACTIVE PATIENTS",
                    value = "${patients.size}",
                    icon = Icons.Default.Group,
                    color = Primary,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            // Recent Patients Section
            Text("Clinical Dashboard", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar for Surgeons
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by name or ID...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = OutlineVariant
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Patient Table Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SurfaceContainerLow,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("ID", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(0.5f), color = OnSurfaceVariant)
                    Text("NAME", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f), color = OnSurfaceVariant)
                    Text("STATUS", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(0.8f), color = OnSurfaceVariant)
                    Text("ACTION", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(0.5f), color = OnSurfaceVariant)
                }
            }
            
            if (loading) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else {
                filteredPatients.forEach { patient ->
                    PatientTableRow(patient, onViewCbct) {
                        scope.launch {
                            try {
                                apiService.togglePatientStatus(patient.id)
                                patients = apiService.getPatients()
                            } catch (e: Exception) { e.printStackTrace() }
                        }
                    }
                }
            }
        } else {
            // User View: Search Records
            Text("Access Your Records", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter Patient ID (e.g. P-12345)") },
                trailingIcon = { 
                    IconButton(onClick = {
                        scope.launch {
                            loading = true
                            try {
                                patientNotes = apiService.getPatientNotes(searchQuery)
                                val statusRes = apiService.getUserPatientStatus(searchQuery)
                                userStatusLabel = statusRes["status"] ?: "Verified Patient"
                            } catch (e: Exception) { e.printStackTrace() }
                            finally { loading = false }
                        }
                    }) {
                        Icon(Icons.Default.Search, null, tint = Primary)
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Clinical Remarks & Reports", style = MaterialTheme.typography.headlineSmall, color = Primary)
            Spacer(modifier = Modifier.height(16.dp))
            
            if (loading) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (patientNotes.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = SurfaceContainerLow
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.History, null, tint = Color.LightGray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No records found for this ID.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                }
            } else {
                patientNotes.forEach { note ->
                    ClinicalNoteCard(note)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        // Quick Actions Panel
        Text("Quick Actions", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = SurfaceContainerLowest,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
        ) {
            Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionTile(Icons.Default.UploadFile, "UPLOAD SCAN", Modifier.weight(1f), onClick = onViewCbct)
                ActionTile(Icons.Default.Analytics, "PROGNOSIS", Modifier.weight(1f), isSecondary = true, onClick = onViewPrognosis)
                ActionTile(Icons.Default.Psychology, "SUPPORT", Modifier.weight(1f), onClick = onViewNerve)
                ActionTile(Icons.Default.MonitorHeart, "TUMOR", Modifier.weight(1f), isSecondary = true, onClick = onViewTumor)
            }
        }

        if (userRole == "Admin") {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onViewAdmin, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("ADMIN PORTAL") }
                Button(onClick = onViewTraining, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Secondary)) { Text("TRAIN AI") }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }

    if (isAddModalOpen) {
        AlertDialog(
            onDismissRequest = { isAddModalOpen = false },
            title = { Text("Register New Patient", fontWeight = FontWeight.Black) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newPatientName,
                        onValueChange = { newPatientName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPatientAge,
                        onValueChange = { newPatientAge = it },
                        label = { Text("Age") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPatientIdToLink,
                        onValueChange = { newPatientIdToLink = it },
                        label = { Text("Link to User ID (Optional)") },
                        placeholder = { Text("e.g. P-12345") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Linking allows the patient to view their own records.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isAdding = true
                        scope.launch {
                            try {
                                val patientData = com.oralsurgeryai.app.data.PatientCreate(
                                    name = newPatientName,
                                    age = newPatientAge.toIntOrNull() ?: 0,
                                    patientId = if (newPatientIdToLink.isBlank()) null else newPatientIdToLink
                                )
                                apiService.createPatient(patientData)
                                patients = apiService.getPatients()
                                isAddModalOpen = false
                                newPatientName = ""
                                newPatientAge = ""
                                newPatientIdToLink = ""
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                isAdding = false
                            }
                        }
                    },
                    enabled = !isAdding && newPatientName.isNotBlank()
                ) {
                    if (isAdding) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    else Text("Register")
                }
            },
            dismissButton = {
                TextButton(onClick = { isAddModalOpen = false }) { Text("Cancel") }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun PatientTableRow(patient: Patient, onOpenScan: () -> Unit, onToggleStatus: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceContainerLowest,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, OutlineVariant.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.6f)) {
                Text(patient.id, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 9.sp)
                Text(patient.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }
            
            Box(modifier = Modifier.weight(0.8f), contentAlignment = Alignment.Center) {
                Surface(
                    color = if (patient.isActive) Primary.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.clickable { onToggleStatus() }
                ) {
                    Text(
                        if (patient.isActive) "ACTIVE" else "CLOSED",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (patient.isActive) Primary else Color.Gray,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
            
            IconButton(onClick = onOpenScan, modifier = Modifier.weight(0.3f).size(24.dp)) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, null, tint = Secondary, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun ClinicalNoteCard(note: ClinicalNote) {
    val isReport = note.content.startsWith("PROGNOSIS_REPORT:")
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceContainerLow,
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = (if (isReport) Secondary else Primary).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        if (isReport) "AI PROGNOSIS REPORT" else "DOCTOR REMARK",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isReport) Secondary else Primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Text(
                    note.timestamp.substringBefore("T"),
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Text("Dr. ${note.doctorName}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            
            if (isReport) {
                Text(
                    "AI Analysis results available in clinical portal.",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            } else {
                Text(note.content, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
            }
        }
    }
}

@Composable
fun ActionTile(icon: ImageVector, label: String, modifier: Modifier, isSecondary: Boolean = false, onClick: () -> Unit = {}) {
    Surface(
        modifier = modifier.height(100.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = SurfaceContainerLow,
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = if (isSecondary) Secondary else Primary, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, fontSize = 9.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun KPICard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(24.dp),
        color = SurfaceContainerLowest,
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant, fontSize = 9.sp)
                Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
            }
            Text(value, style = MaterialTheme.typography.displayLarge, fontSize = 28.sp, color = OnSurface)
            if (title == "DIAGNOSTIC ACCURACY") {
                Text("DICE VERIFIED", style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Black, fontSize = 8.sp)
            }
            LinearProgressIndicator(
                progress = { 0.76f },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                color = color,
                trackColor = SurfaceContainer
            )
        }
    }
}
