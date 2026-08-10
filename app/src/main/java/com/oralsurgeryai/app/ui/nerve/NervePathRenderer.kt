package com.oralsurgeryai.app.ui.nerve

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.oralsurgeryai.app.ui.theme.Secondary
import com.oralsurgeryai.app.ui.theme.WarningOrange

enum class NerveViewMode {
    CENTERLINE,
    CENTERLINE_WITH_POINTS,
    CANAL_OVERLAY,
    OVERLAY_ONLY
}

@Composable
fun NervePathRenderer(
    points: List<NervePoint>,
    currentSliceIndex: Int,
    viewMode: NerveViewMode = NerveViewMode.CENTERLINE,
    viewType: String = "Axial", // "Axial", "Coronal", "Sagittal"
    thickness: Float = 4f,
    opacity: Float = 0.8f,
    implantSites: List<ImplantSite> = emptyList()
) {
    val path = SplineCalculator.generateSmoothPath(points)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeStyle = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        // 1. AXIAL VIEW: Continuous Line Projection
        if (viewType == "Axial") {
            // Draw Main Centerline (High-Contrast Yellow for Clinical Use)
            drawPath(
                path = path,
                color = Color.Yellow.copy(alpha = opacity),
                style = strokeStyle
            )

            // Draw Canal Overlay
            if (viewMode == NerveViewMode.CANAL_OVERLAY || viewMode == NerveViewMode.OVERLAY_ONLY) {
                drawPath(
                    path = path,
                    color = Color.Yellow.copy(alpha = 0.15f * opacity),
                    style = Stroke(width = thickness * 5, cap = StrokeCap.Round)
                )
            }
            
            // DYNAMIC IMPLANT MARKERS
            implantSites.forEach { site ->
                val markerColor = when(site.safetyStatus) {
                    "CRITICAL" -> Color.Red
                    "CAUTION" -> WarningOrange
                    else -> Color.Cyan
                }
                
                // Draw target marker
                drawCircle(
                    color = markerColor,
                    radius = thickness * 3f,
                    center = site.center,
                    style = Stroke(width = 2.dp.toPx())
                )
                
                // Draw proximity line to nerve
                points.find { it.sliceIndex == currentSliceIndex }?.let { nervePt ->
                    val nOffset = Offset(nervePt.x, nervePt.y)
                    drawLine(
                        color = markerColor.copy(alpha = 0.4f),
                        start = site.center,
                        end = nOffset,
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }
            }
        } 
        
        // 2. CORONAL/SAGITTAL: Point-at-Depth Visualization
        else {
            points.find { it.sliceIndex == currentSliceIndex }?.let { pt ->
                // Draw Intersection Crosshair
                drawLine(
                    color = Color.Yellow.copy(alpha = 0.5f),
                    start = Offset(0f, pt.y),
                    end = Offset(size.width, pt.y),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.Yellow.copy(alpha = 0.5f),
                    start = Offset(pt.x, 0f),
                    end = Offset(pt.x, size.height),
                    strokeWidth = 1.dp.toPx()
                )

                // Draw Core Nerve Point
                drawCircle(
                    color = Color.Yellow,
                    radius = (thickness * 1.5f),
                    center = Offset(pt.x, pt.y)
                )
            }
        }

        // 3. Highlight intersection on all views
        points.find { it.sliceIndex == currentSliceIndex }?.let { activePt ->
            drawCircle(
                color = Color.Green,
                radius = thickness * 2f,
                center = Offset(activePt.x, activePt.y),
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}
