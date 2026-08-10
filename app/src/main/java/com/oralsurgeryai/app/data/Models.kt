package com.oralsurgeryai.app.data

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val full_name: String,
    val otp: String
)

data class LoginResponse(
    val status: String,
    val user: UserInfo
)

data class UserInfo(
    val name: String,
    val email: String,
    val role: String,
    val patient_id: String? = null
)

data class Patient(
    val id: String,
    val name: String,
    val age: Int,
    val date: String,
    val risk: String,
    @SerializedName("is_active") val isActive: Boolean = true
)

data class PatientCreate(
    val name: String,
    val age: Int,
    @SerializedName("patient_id") val patientId: String? = null
)

data class ClinicalData(
    val age: Int,
    @SerializedName("smoking_history") val smokingHistory: Int,
    @SerializedName("alcohol_history") val alcoholHistory: Int,
    @SerializedName("tumor_size_cm") val tumorSizeCm: Double,
    @SerializedName("lymph_node_involvement") val lymphNodeInvolvement: Int,
    @SerializedName("hpv_status") val hpvStatus: Int,
    @SerializedName("ian_invasion_detected") val ianInvasionDetected: Int
)

data class PrognosisResponse(
    @SerializedName("prediction_class") val predictionClass: Int,
    val probability: Double,
    @SerializedName("survival_2yr") val survival2yr: String,
    @SerializedName("risk_stratification") val riskStratification: String,
    @SerializedName("decision_support") val decisionSupport: DecisionSupport,
    @SerializedName("top_features") val topFeatures: List<FeatureImpact>,
    @SerializedName("raw_input") val rawInput: ClinicalData?,
    @SerializedName("shap_image") val shapImage: String? // Base64
)

data class DecisionSupport(
    @SerializedName("suggested_plan") val suggestedPlan: String,
    val confidence: String,
    val note: String
)

data class FeatureImpact(
    val feature: String,
    val impact: String
)

data class CbctResponse(
    val status: String,
    val message: String,
    val mode: String,
    @SerializedName("file_name") val fileName: String? = null,
    @SerializedName("slices_raw") val slicesRaw: List<String>,
    @SerializedName("slices_right") val slicesRight: List<String>,
    @SerializedName("slices_left") val slicesLeft: List<String>,
    @SerializedName("slices_tumor") val slicesTumor: List<String>,
    @SerializedName("clinical_perspectives") val clinicalPerspectives: ClinicalPerspectives,
    @SerializedName("time_metrics") val timeMetrics: TimeMetrics,
    @SerializedName("clinical_interpretation") val clinicalInterpretation: String?,
    @SerializedName("existing_notes") val existingNotes: List<ClinicalNote>? = null
)

data class NoteRequest(
    @SerializedName("file_name") val fileName: String? = null,
    @SerializedName("patient_id") val patientId: String? = null,
    @SerializedName("doctor_name") val doctorName: String,
    @SerializedName("doctor_email") val doctorEmail: String,
    val content: String
)

data class ClinicalNote(
    val id: Int,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("doctor_name") val doctorName: String,
    @SerializedName("doctor_email") val doctorEmail: String,
    val content: String,
    val timestamp: String
)

data class PerspectiveLayer(
    val raw: String,
    val nerve: String,
    val tumor: String
)

data class TimeMetrics(
    @SerializedName("ai_time_seconds") val aiTimeSeconds: Double,
    @SerializedName("manual_time") val manualTime: String,
    @SerializedName("efficiency_gain") val efficiencyGain: String
)

data class OralHealthReviewResponse(
    @SerializedName("overall_rating") val overallRating: Int,
    @SerializedName("rating_label") val ratingLabel: String,
    @SerializedName("teeth_findings") val teethFindings: List<DentalHealthAnalysis>,
    @SerializedName("summary_paragraph") val summaryParagraph: String,
    val recommendations: List<String>,
    @SerializedName("contributing_factors") val contributingFactors: List<String>,
    @SerializedName("annotated_image") val annotatedImage: String? // Base64
)

data class DentalHealthAnalysis(
    @SerializedName("tooth_no") val toothNo: Int,
    @SerializedName("tooth_type") val toothType: String,
    val status: String,
    val severity: String,
    val condition: String,
    val confidence: Int
)

data class ClinicalPerspectives(
    @SerializedName("axial_stack") val axialStack: List<PerspectiveLayer>,
    @SerializedName("coronal_stack") val coronalStack: List<PerspectiveLayer>,
    @SerializedName("sagittal_stack") val sagittalStack: List<PerspectiveLayer>,
    @SerializedName("Panoramic") val panoramic: PerspectiveLayer?,
    @SerializedName("Frontal") val frontal: PerspectiveLayer?,
    @SerializedName("initial_indices") val initialIndices: Map<String, Int>,
    @SerializedName("max_indices") val maxIndices: Map<String, Int>
)
