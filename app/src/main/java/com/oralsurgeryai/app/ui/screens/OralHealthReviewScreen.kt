package com.oralsurgeryai.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oralsurgeryai.app.data.DentalHealthAnalysis
import com.oralsurgeryai.app.data.NetworkModule
import com.oralsurgeryai.app.data.OralHealthReviewResponse
import com.oralsurgeryai.app.ui.theme.*

@Composable
fun OralHealthReviewScreen(cbctResponse: com.oralsurgeryai.app.data.CbctResponse?) {
    var result by remember { mutableStateOf<OralHealthReviewResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showRecommendations by remember { mutableStateOf(false) }
    var errorOccurred by remember { mutableStateOf(false) }

    LaunchedEffect(cbctResponse) {
        if (cbctResponse != null) {
            isLoading = true
            try {
                result = NetworkModule.apiService.analyzeOralHealth()
                errorOccurred = false
            } catch (e: Exception) {
                errorOccurred = true
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("ADVANCED ORAL HEALTH REVIEW", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))

        if (cbctResponse == null) {
            // STATE: NO SCAN UPLOADED
            Surface(
                modifier = Modifier.fillMaxWidth().height(450.dp),
                shape = RoundedCornerShape(32.dp),
                color = SurfaceContainerLowest,
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.size(80.dp).background(Secondary.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.UploadFile, null, tint = Secondary, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Awaiting Volumetric Scan", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Clinical diagnostics cannot be generated without an active CBCT volume. Please upload a scan to proceed.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = OnSurfaceVariant
                    )
                }
            }
        } else if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Secondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Synthesizing anatomical markers...", style = MaterialTheme.typography.labelSmall)
                }
            }
        } else if (result != null) {
            // Section 1: Oral Health Rating
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = SurfaceContainerLowest,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Overall Oral Health Score", style = MaterialTheme.typography.titleMedium, color = OnSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = result!!.overallRating / 100f,
                            modifier = Modifier.size(120.dp),
                            strokeWidth = 10.dp,
                            color = getRatingColor(result!!.overallRating),
                            trackColor = SurfaceContainerHigh
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${result!!.overallRating}/100", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
                            Text(result!!.ratingLabel, style = MaterialTheme.typography.labelLarge, color = getRatingColor(result!!.overallRating))
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Contributing Factors:", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant, modifier = Modifier.align(Alignment.Start))
                    Spacer(modifier = Modifier.height(8.dp))
                    result!!.contributingFactors.forEach { factor ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Adjust, null, tint = Secondary, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(factor, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 1. Panoramic Representation (Axial View for FDI mapping)
            Surface(
                modifier = Modifier.fillMaxWidth().aspectRatio(2f),
                shape = RoundedCornerShape(16.dp),
                color = Color.Black
            ) {
                cbctResponse?.clinicalPerspectives?.let { cp ->
                    val axialIndex = cp.initialIndices["axial"] ?: 0
                    cp.axialStack.getOrNull(axialIndex)?.let {
                        CommonUi.Base64Image(it.raw, Modifier.fillMaxSize())
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 3: Interactive Dental Chart (Simplified FDI Table)
            Text("TOOTH-LEVEL DIAGNOSTICS (FDI)", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = SurfaceContainerLowest,
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
            ) {
                Column {
                    Row(modifier = Modifier.background(SurfaceContainerLow).padding(16.dp)) {
                        Text("No", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                        Text("Condition", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1.2f), fontWeight = FontWeight.Bold)
                        Text("Severity", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold)
                        Text("Confidence", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold)
                    }
                    result!!.teethFindings.forEach { finding ->
                        ToothAnalysisRow(finding)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 4: Clinical Summary
            Text("AI CLINICAL ASSESSMENT", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF8FAFF),
                border = androidx.compose.foundation.BorderStroke(1.dp, Secondary.copy(alpha = 0.2f))
            ) {
                Text(
                    text = result!!.summaryParagraph,
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 5: Recommendations
            Button(
                onClick = { showRecommendations = !showRecommendations },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Icon(Icons.Default.Lightbulb, null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(if (showRecommendations) "HIDE RECOMMENDATIONS" else "VIEW RECOMMENDATIONS & NEXT STEPS", fontWeight = FontWeight.Bold)
            }

            AnimatedVisibility(visible = showRecommendations) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    RecommendationSection("Immediate Actions", result!!.recommendations.take(2), Icons.Default.PriorityHigh, Error)
                    Spacer(modifier = Modifier.height(12.dp))
                    RecommendationSection("Preventive Advice", result!!.recommendations.drop(2), Icons.Default.Shield, Secondary)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            Text(
                "Disclaimer: AI-generated findings are for informational purposes only and do not replace diagnosis by a qualified dentist or oral surgeon.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun ToothAnalysisRow(finding: DentalHealthAnalysis) {
    var showDetail by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.clickable { showDetail = !showDetail }) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("#${finding.toothNo}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1.2f)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(getStatusColor(finding.status)))
                Spacer(modifier = Modifier.width(8.dp))
                Text(finding.status, style = MaterialTheme.typography.bodySmall)
            }
            Text(finding.severity, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.8f), color = getSeverityColor(finding.severity))
            Text("${finding.confidence}%", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.8f), color = Secondary)
        }
        
        AnimatedVisibility(visible = showDetail) {
            Surface(color = SurfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Type: ${finding.toothType}", style = MaterialTheme.typography.labelLarge)
                    Text("Condition: ${finding.condition}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Divider(color = OutlineVariant.copy(alpha = 0.2f), thickness = 0.5.dp)
    }
}

@Composable
fun RecommendationSection(title: String, items: List<String>, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Black, fontSize = 12.sp, color = color)
            }
            Spacer(modifier = Modifier.height(12.dp))
            items.forEach { item ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("•", style = MaterialTheme.typography.bodySmall, color = color)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(item, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

fun getStatusColor(status: String): Color {
    return when(status) {
        "Healthy" -> Secondary
        "Cavity" -> WarningOrange
        "Fractured" -> Error
        "Missing" -> Color.Black
        "Implant", "Crown" -> Color(0xFF007AFF)
        else -> Color.Gray
    }
}

fun getSeverityColor(severity: String): Color {
    return when(severity) {
        "Severe" -> Error
        "Moderate" -> WarningOrange
        "Mild" -> Color.Gray
        else -> Secondary
    }
}

fun getRatingColor(rating: Int): Color {
    return when {
        rating >= 90 -> Secondary
        rating >= 75 -> Color(0xFF007AFF)
        rating >= 60 -> WarningOrange
        else -> Error
    }
}
