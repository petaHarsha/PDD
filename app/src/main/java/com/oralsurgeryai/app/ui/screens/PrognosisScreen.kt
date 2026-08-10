package com.oralsurgeryai.app.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oralsurgeryai.app.data.ClinicalData
import com.oralsurgeryai.app.data.NetworkModule
import com.oralsurgeryai.app.data.NoteRequest
import com.oralsurgeryai.app.data.PrognosisResponse
import com.oralsurgeryai.app.data.UserSession
import com.oralsurgeryai.app.ui.theme.*
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun PrognosisScreen(
    cbctResponse: com.oralsurgeryai.app.data.CbctResponse? = null,
    onViewReport: () -> Unit,
    onAnalysisComplete: (PrognosisResponse) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isSurgeon = UserSession.userRole == "Surgeon"
    
    var targetPatientId by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("45") }
    var tumorSize by remember { mutableStateOf("2.5") }
    var smoking by remember { mutableStateOf(false) }
    var alcohol by remember { mutableStateOf(false) }
    var lymphNode by remember { mutableStateOf(false) }
    var hpvStatus by remember { mutableStateOf(false) }
    var ianInvasion by remember { mutableStateOf(false) }
    
    var isLoading by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<PrognosisResponse?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("PREDICTIVE ANALYTICS", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Recovery Trajectory", style = MaterialTheme.typography.displayLarge, fontSize = 28.sp)

        Spacer(modifier = Modifier.height(32.dp))

        if (cbctResponse == null) {
            // Empty State
            Surface(
                modifier = Modifier.fillMaxWidth().height(400.dp),
                shape = RoundedCornerShape(24.dp),
                color = SurfaceContainerLowest,
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Analytics, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Awaiting Scan Volume", style = MaterialTheme.typography.headlineMedium, color = Color.Gray)
                    Text("Please process a CBCT scan before running prognosis.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Form Section
                Surface(
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(24.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EditNote, null, tint = Primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Patient Parameters", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        if (isSurgeon) {
                            OutlinedTextField(
                                value = targetPatientId,
                                onValueChange = { targetPatientId = it },
                                label = { Text("Target Patient ID") },
                                placeholder = { Text("e.g. P-12345") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = CircleShape,
                                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Secondary.copy(alpha = 0.05f), focusedContainerColor = Secondary.copy(alpha = 0.05f), unfocusedBorderColor = Secondary.copy(alpha = 0.2f))
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text("Age") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape,
                            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = SurfaceContainerLow, focusedContainerColor = SurfaceContainerLow, unfocusedBorderColor = Color.Transparent)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = tumorSize,
                            onValueChange = { tumorSize = it },
                            label = { Text("Tumor Size (cm)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape,
                            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = SurfaceContainerLow, focusedContainerColor = SurfaceContainerLow, unfocusedBorderColor = Color.Transparent)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Risk Factors", style = MaterialTheme.typography.labelLarge, color = Secondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        RiskToggle("Tobacco Use", smoking) { smoking = it }
                        RiskToggle("Alcohol Intake", alcohol) { alcohol = it }
                        RiskToggle("Lymph Nodes", lymphNode) { lymphNode = it }
                        RiskToggle("HPV Positive", hpvStatus) { hpvStatus = it }
                        RiskToggle("IAN Invasion", ianInvasion) { ianInvasion = it }

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = {
                                isLoading = true
                                scope.launch {
                                    try {
                                        val data = ClinicalData(
                                            age = age.toIntOrNull() ?: 45,
                                            smokingHistory = if (smoking) 1 else 0,
                                            alcoholHistory = if (alcohol) 1 else 0,
                                            tumorSizeCm = tumorSize.toDoubleOrNull() ?: 0.1,
                                            lymphNodeInvolvement = if (lymphNode) 1 else 0,
                                            hpvStatus = if (hpvStatus) 1 else 0,
                                            ianInvasionDetected = if (ianInvasion) 1 else 0
                                        )
                                        val response = NetworkModule.apiService.predictPrognosis(data)
                                        result = response
                                        onAnalysisComplete(response)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                            else Text("RECALCULATE", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Probability Card
                Surface(
                    modifier = Modifier.weight(0.8f).height(IntrinsicSize.Min),
                    shape = RoundedCornerShape(24.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text("RECOVERY", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { result?.probability?.toFloat() ?: 0.84f },
                                modifier = Modifier.size(100.dp),
                                color = Secondary,
                                strokeWidth = 8.dp,
                                trackColor = SurfaceContainerHigh
                            )
                            Text("${((result?.probability ?: 0.84) * 100).toInt()}%", style = MaterialTheme.typography.displayLarge, fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(result?.riskStratification ?: "LOW", color = Secondary, fontWeight = FontWeight.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI Recommendation Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = SurfaceContainerLowest,
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).background(Primary, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Psychology, null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("AI Prognosis Recommendation", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Surface(
                        color = SurfaceContainerLow,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Secondary.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = result?.decisionSupport?.suggestedPlan ?: "Based on clinical morphology, functional recovery is predicted within 12 months. Standard surgical margin of 10mm recommended.",
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = onViewReport, modifier = Modifier.fillMaxWidth(), shape = CircleShape) {
                        Text("VIEW FULL SURGICAL REPORT")
                    }
                    
                    if (isSurgeon && result != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                if (targetPatientId.isBlank()) {
                                    Toast.makeText(context, "Please enter a Patient ID", Toast.LENGTH_SHORT).show()
                                    return@OutlinedButton
                                }
                                isSaving = true
                                scope.launch {
                                    try {
                                        val reportJson = Gson().toJson(result)
                                        val request = NoteRequest(
                                            patientId = targetPatientId,
                                            doctorName = UserSession.userName,
                                            doctorEmail = UserSession.userEmail,
                                            content = "PROGNOSIS_REPORT:$reportJson"
                                        )
                                        NetworkModule.apiService.saveClinicalNote(request)
                                        Toast.makeText(context, "Report saved to cloud", Toast.LENGTH_SHORT).show()
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Failed to save: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isSaving = false
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = CircleShape,
                            enabled = !isSaving
                        ) {
                            if (isSaving) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Primary)
                            else {
                                Icon(Icons.Default.CloudUpload, null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("SAVE TO PATIENT RECORD")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun RiskToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Switch(
            checked = checked, 
            onCheckedChange = onCheckedChange,
            modifier = Modifier.scale(0.7f),
            colors = SwitchDefaults.colors(checkedThumbColor = Secondary)
        )
    }
}
