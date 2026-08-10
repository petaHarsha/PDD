package com.oralsurgeryai.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oralsurgeryai.app.ui.theme.Primary
import com.oralsurgeryai.app.ui.theme.Secondary
import com.oralsurgeryai.app.ui.theme.SecondaryContainer

@Composable
fun ClinicalLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(100.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scale = size.width / 100f
            val centerOffset = Offset(size.width / 2, size.height / 2)

            // Outer Black Circle - radius decreased and thickness increased to match green
            drawCircle(
                color = Color.Black,
                radius = 42f * scale,
                center = centerOffset,
                style = Stroke(width = 12f * scale)
            )

            // Inner Green Circle
            drawCircle(
                color = Color(0xFF0D7A57),
                radius = 26f * scale,
                center = centerOffset
            )

            // Center White Circle for Plus Sign
            drawCircle(
                color = Color.White,
                radius = 14f * scale,
                center = centerOffset
            )

            // Plus Sign
            drawLine(
                color = Color.Black,
                start = Offset(50f * scale, 42f * scale),
                end = Offset(50f * scale, 58f * scale),
                strokeWidth = 3f * scale,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.Black,
                start = Offset(42f * scale, 50f * scale),
                end = Offset(58f * scale, 50f * scale),
                strokeWidth = 3f * scale,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun ClinicalBranding(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClinicalLogo()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "ORAL SURGERY AI",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Clinical Intelligence Systems",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
