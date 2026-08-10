package com.oralsurgeryai.app.ui.nerve

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlin.math.*

data class NervePoint(
    val x: Float,
    val y: Float,
    val sliceIndex: Int,
    val confidence: Float
)

/**
 * Advanced Spline Calculator for Anatomical Curves.
 * Implements Catmull-Rom Spline for continuous, smooth nerve paths.
 */
object SplineCalculator {

    fun generateSmoothPath(points: List<NervePoint>, resolution: Int = 10): Path {
        if (points.size < 2) return Path()

        val path = Path()
        val sortedPoints = points.sortedBy { it.sliceIndex }

        // Start Path
        path.moveTo(sortedPoints[0].x, sortedPoints[0].y)

        for (i in 0 until sortedPoints.size - 1) {
            val p0 = if (i == 0) sortedPoints[i] else sortedPoints[i - 1]
            val p1 = sortedPoints[i]
            val p2 = sortedPoints[i + 1]
            val p3 = if (i + 2 < sortedPoints.size) sortedPoints[i + 2] else p2

            for (step in 1..resolution) {
                val t = step.toFloat() / resolution
                val pos = catmullRom(p0, p1, p2, p3, t)
                path.lineTo(pos.x, pos.y)
            }
        }

        return path
    }

    private fun catmullRom(p0: NervePoint, p1: NervePoint, p2: NervePoint, p3: NervePoint, t: Float): Offset {
        val t2 = t * t
        val t3 = t2 * t
        
        val f1 = -0.5f * t3 + t2 - 0.5f * t
        val f2 = 1.5f * t3 - 2.5f * t2 + 1.0f
        val f3 = -1.5f * t3 + 2.0f * t2 + 0.5f * t
        val f4 = 0.5f * t3 - 0.5f * t2

        val x = p0.x * f1 + p1.x * f2 + p2.x * f3 + p3.x * f4
        val y = p0.y * f1 + p1.y * f2 + p2.y * f3 + p3.y * f4

        return Offset(x, y)
    }

    fun calculateNerveLength(points: List<NervePoint>, voxelSizeMm: Float = 0.3f): Float {
        var totalDist = 0f
        for (i in 0 until points.size - 1) {
            val p1 = points[i]
            val p2 = points[i+1]
            val dx = p2.x - p1.x
            val dy = p2.y - p1.y
            val dz = (p2.sliceIndex - p1.sliceIndex).toFloat()
            totalDist += sqrt(dx*dx + dy*dy + dz*dz)
        }
        return totalDist * voxelSizeMm
    }
}
