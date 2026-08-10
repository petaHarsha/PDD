package com.oralsurgeryai.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oralsurgeryai.app.data.CbctResponse
import com.oralsurgeryai.app.data.PrognosisResponse
import com.oralsurgeryai.app.ui.theme.*

@Composable
fun ReportsScreen(
    cbctResponse: CbctResponse? = null,
    prognosisResponse: PrognosisResponse? = null,
    onNavigateToOralHealth: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("CLINICAL ARCHIVE", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Surgical Reports", style = MaterialTheme.typography.displayLarge, fontSize = 28.sp)

        Spacer(modifier = Modifier.height(32.dp))

        if (cbctResponse == null && prognosisResponse == null) {
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
                    Icon(Icons.Default.FolderOpen, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No Active Records", style = MaterialTheme.typography.headlineMedium, color = Color.Gray)
                    Text("Processed scans and prognosis results will appear here.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        } else {
            // Summary Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Primary,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("CASE SUMMARY", color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.labelSmall)
                    Text("Patient: Michael R. (P-48291)", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        ReportField("PROCEDURE", "Extraction #48", Color.White)
                        ReportField("AI STATUS", if (cbctResponse != null) "VERIFIED" else "PENDING", Color.White)
                        ReportField("DATE", "Oct 24, 2024", Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Diagnostic Findings
            Text("DIAGNOSTIC FINDINGS", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = SurfaceContainerLowest,
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    FindingRow("IAN Relationship", if (cbctResponse != null) "Stage 4 (Contact)" else "N/A", Icons.Default.AltRoute)
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = OutlineVariant.copy(alpha = 0.2f))
                    FindingRow("Voxel Density", "1250 HU (Cortical)", Icons.Default.Texture)
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = OutlineVariant.copy(alpha = 0.2f))
                    FindingRow("Malignancy Risk", prognosisResponse?.riskStratification ?: "Low", Icons.Default.Warning)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Actions
            Button(
                onClick = onNavigateToOralHealth,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.AutoGraph, null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("GENERATE ADVANCED ORAL REVIEW")
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedButton(
                onClick = { /* Print logic */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Print, null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("EXPORT PDF SURGICAL REPORT")
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun ReportField(label: String, value: String, color: Color) {
    Column {
        Text(label, color = color.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(value, color = color, fontSize = 12.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun FindingRow(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(36.dp).background(SurfaceContainerLow, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Secondary, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Black, color = Primary)
    }
}
