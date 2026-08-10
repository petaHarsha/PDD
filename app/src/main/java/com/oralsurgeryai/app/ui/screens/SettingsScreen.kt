package com.oralsurgeryai.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.oralsurgeryai.app.ui.theme.*

@Composable
fun SettingsScreen(onLogout: () -> Unit = {}) {
    val userRole = com.oralsurgeryai.app.data.UserSession.userRole
    val isUser = userRole == "User"
    val patientId = com.oralsurgeryai.app.data.UserSession.patientId
    
    var userStatusLabel by remember { mutableStateOf("Verified Patient") }
    
    val apiService = com.oralsurgeryai.app.data.NetworkModule.apiService
    
    LaunchedEffect(Unit) {
        if (isUser && patientId != null) {
            try {
                val statusRes = apiService.getUserPatientStatus(patientId)
                userStatusLabel = statusRes["status"] ?: "Verified Patient"
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    var isDarkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    
    var showDisclaimerDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    if (showDisclaimerDialog) {
        AlertDialog(
            onDismissRequest = { showDisclaimerDialog = false },
            title = { Text("Clinical Decision Disclaimer", fontWeight = FontWeight.Black) },
            text = {
                Text(
                    "Oral Surgery AI is a clinical decision support tool designed to assist healthcare professionals. " +
                    "The system does not replace professional medical judgment. All diagnostic findings, treatment plans, " +
                    "and surgical decisions must be reviewed and validated by a qualified clinician before implementation. " +
                    "\n\nVersion: 4.2.1-stable • HIPAA Compliant Engine"
                )
            },
            confirmButton = {
                Button(onClick = { showDisclaimerDialog = false }) {
                    Text("Close")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About Oral Surgery AI", fontWeight = FontWeight.Black) },
            text = {
                Text(
                    "A state-of-the-art AI platform specialized in maxillofacial radiological analysis. " +
                    "\n\nModules:\n" +
                    "• Penta-Planar IAN Localization\n" +
                    "• Volumetric Lesion Characterization\n" +
                    "• XGBoost Predictive Prognosis\n\n" +
                    "Developed for academic and clinical research excellence."
                )
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }) {
                    Text("Done")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("SYSTEM SETTINGS", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = SurfaceContainerLowest,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
        ) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Secondary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = Secondary, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(com.oralsurgeryai.app.data.UserSession.userName, style = MaterialTheme.typography.headlineMedium, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(com.oralsurgeryai.app.data.UserSession.userEmail, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                    
                    Surface(
                        color = (if (isUser && userStatusLabel == "Active Patient") Primary else Secondary).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            if (isUser) userStatusLabel.uppercase() else com.oralsurgeryai.app.data.UserSession.userRole.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isUser && userStatusLabel == "Active Patient") Primary else Secondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        if (isUser && patientId != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Primary.copy(alpha = 0.05f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Primary.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("YOUR PATIENT ID", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
                    Text(patientId, style = MaterialTheme.typography.headlineSmall, color = Primary, fontWeight = FontWeight.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Preferences
        if (!isUser) {
            SettingsGroup("Clinical Preferences") {
                PreferenceToggle(Icons.Default.DarkMode, "Clinical Dark Mode", isDarkMode) { isDarkMode = it }
                PreferenceToggle(Icons.Default.NotificationsActive, "Processing Alerts", notificationsEnabled) { notificationsEnabled = it }
                SettingsItem(Icons.Default.PrecisionManufacturing, "AI Model Version", "V4.2.1 Stable") {}
            }
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            SettingsGroup("App Settings") {
                PreferenceToggle(Icons.Default.DarkMode, "Dark Mode", isDarkMode) { isDarkMode = it }
                PreferenceToggle(Icons.Default.NotificationsActive, "Notifications", notificationsEnabled) { notificationsEnabled = it }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Legal
        SettingsGroup("Compliance & Security") {
            SettingsItem(Icons.Default.VerifiedUser, "HIPAA Encryption", "Active") {}
            SettingsItem(Icons.Default.Description, "Clinical Disclaimer", "") { showDisclaimerDialog = true }
            SettingsItem(Icons.Default.Info, "About Oral Surgery AI", "") { showAboutDialog = true }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { 
                com.oralsurgeryai.app.data.UserSession.clear()
                onLogout() 
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Error, contentColor = OnError)
        ) {
            Text("SECURE LOGOUT", fontWeight = FontWeight.Black)
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun PreferenceToggle(icon: ImageVector, title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = Secondary))
    }
}

@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.labelLarge, color = Secondary, modifier = Modifier.padding(start = 8.dp, bottom = 12.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = SurfaceContainerLowest,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
        if (subtitle.isNotEmpty()) {
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Secondary, fontWeight = FontWeight.Bold)
        } else {
            Icon(Icons.Default.ChevronRight, null, tint = OutlineVariant)
        }
    }
}
