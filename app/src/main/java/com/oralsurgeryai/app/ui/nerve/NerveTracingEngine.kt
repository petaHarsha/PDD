package com.oralsurgeryai.app.ui.nerve

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.Color
import com.oralsurgeryai.app.data.CbctResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ClinicalMetrics(
    val lengthMm: Float,
    val avgConfidence: Float,
    val sliceCount: Int,
    val interpolationRate: Float,
    val curvatureIndex: Float
)

class NerveTracingEngine {

    suspend fun extractNervePath(cbctResponse: CbctResponse): List<NervePoint> = withContext(Dispatchers.Default) {
        val points = mutableListOf<NervePoint>()
        
        cbctResponse.slicesRight.forEachIndexed { index, base64 ->
            val bitmap = decodeBase64(base64)
            bitmap?.let { 
                val center = findCenterOfNerve(it)
                if (center != null) {
                    points.add(NervePoint(center.first, center.second, index, 1.0f))
                }
            }
        }
        
        return@withContext points
    }

    private fun decodeBase64(base64: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }

    private fun findCenterOfNerve(bitmap: Bitmap): Pair<Float, Float>? {
        var sumX = 0f
        var sumY = 0f
        var count = 0
        
        // Sample every 2nd pixel for performance
        for (x in 0 until bitmap.width step 2) {
            for (y in 0 until bitmap.height step 2) {
                val pixel = bitmap.getPixel(x, y)
                val alpha = (pixel shr 24) and 0xff
                if (alpha > 50) { // If pixel is not transparent
                    sumX += x
                    sumY += y
                    count++
                }
            }
        }
        
        return if (count > 0) {
            Pair(sumX / count, sumY / count)
        } else {
            null
        }
    }

    fun calculateMetrics(points: List<NervePoint>): ClinicalMetrics {
        val length = SplineCalculator.calculateNerveLength(points)
        return ClinicalMetrics(
            lengthMm = length,
            avgConfidence = points.map { it.confidence }.average().toFloat(),
            sliceCount = points.size,
            interpolationRate = 0.2f, // Simplified placeholder
            curvatureIndex = 1.15f // Simplified placeholder
        )
    }
}
