package com.oralsurgeryai.app.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oralsurgeryai.app.ui.theme.SurfaceContainerLow

object CommonUi {
    @Composable
    fun Base64Image(base64: String, modifier: Modifier = Modifier, alpha: Float = 1f) {
        if (base64.isEmpty()) return
        
        val bitmap = remember(base64.hashCode()) {
            try {
                val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                null
            }
        }

        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "AI View",
                modifier = modifier,
                contentScale = ContentScale.Fit,
                alpha = alpha
            )
        }
    }

    @Composable
    fun MetricMiniCard(label: String, value: String, modifier: Modifier = Modifier, color: Color = Color.Black) {
        Surface(
            modifier = modifier.height(80.dp),
            shape = RoundedCornerShape(16.dp),
            color = color.copy(alpha = 0.1f),
            border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold, fontSize = 8.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(value, style = MaterialTheme.typography.headlineSmall, color = color, fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
        }
    }
}
