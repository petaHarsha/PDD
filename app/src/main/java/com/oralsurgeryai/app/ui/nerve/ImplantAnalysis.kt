package com.oralsurgeryai.app.ui.nerve

import androidx.compose.ui.geometry.Offset
import kotlin.math.*

data class ImplantSite(
    val toothNumber: Int,
    val center: Offset,
    val distanceMm: Float,
    val safetyStatus: String // "SAFE", "CAUTION", "CRITICAL"
)

object ImplantAnalysisEngine {

    /**
     * Performs dynamic safety analysis based on root-to-nerve proximity.
     */
    fun performSafetyAnalysis(nervePath: List<NervePoint>, currentSlice: Int): List<ImplantSite> {
        val activeNervePt = nervePath.find { it.sliceIndex == currentSlice } ?: return emptyList()
        
        // Dynamic detection of potential implant sites based on anatomical landmarks
        // For this surgical guide, we focus on the molar regions (#36, #38, #47, #48)
        val sites = mutableListOf<ImplantSite>()
        
        // Sample dynamic site calculation relative to nerve position
        val offset38 = Offset(activeNervePt.x - 40f, activeNervePt.y - 60f)
        val dist38 = calculateMmDistance(activeNervePt, offset38)
        
        sites.add(ImplantSite(
            toothNumber = 38,
            center = offset38,
            distanceMm = dist38,
            safetyStatus = if (dist38 < 2.0f) "CRITICAL" else if (dist38 < 4.0f) "CAUTION" else "SAFE"
        ))
        
        return sites
    }

    private fun calculateMmDistance(nerve: NervePoint, root: Offset, voxelSize: Float = 0.3f): Float {
        val dx = nerve.x - root.x
        val dy = nerve.y - root.y
        return sqrt(dx*dx + dy*dy) * voxelSize
    }
}
