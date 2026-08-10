package com.oralsurgeryai.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oralsurgeryai.app.ui.theme.*

@Composable
fun ClinicalPrecisionAIScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("CLINICAL PRECISION AI", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Precision Analytics", style = MaterialTheme.typography.displayLarge, fontSize = 28.sp)

        Spacer(modifier = Modifier.height(32.dp))

        // Bento Cards
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = SurfaceContainerLowest,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Verified, null, tint = Secondary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Validation Summary", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "The current analysis has been cross-referenced with 14,200 longitudinal case studies from the Clinical Gold Standard Dataset v12.",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                PrecisionMetric("Data Lineage Match", 0.92f)
                Spacer(modifier = Modifier.height(12.dp))
                PrecisionMetric("Interpretability Index", 0.85f)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Primary
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("AI Impression", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Structural morphology analysis confirms high-confidence target localization. Recommended approach: Piezoelectric surgery to minimize neurovascular trauma.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun PrecisionMetric(label: String, progress: Float) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
            Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Secondary)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth().height(6.dp).background(SurfaceContainerHigh, CircleShape),
            color = Secondary,
            trackColor = Color.Transparent
        )
    }
}
