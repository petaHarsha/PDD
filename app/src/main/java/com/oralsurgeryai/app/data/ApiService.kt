package com.oralsurgeryai.app.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/request-registration-otp")
    suspend fun requestRegistrationOtp(@Body request: Map<String, String>): Map<String, String>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Map<String, String>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: Map<String, String>): Map<String, String>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: Map<String, String>): Map<String, String>

    @GET("patients")
    suspend fun getPatients(): List<Patient>

    @POST("predict/prognosis")
    suspend fun predictPrognosis(@Body data: ClinicalData): PrognosisResponse

    @Multipart
    @POST("upload_cbct")
    suspend fun uploadCbct(@Part photo: MultipartBody.Part?, 
        @Part file: MultipartBody.Part,
        @Part("demo_mode") demoMode: RequestBody
    ): CbctResponse

    @POST("train/segmentation")
    suspend fun triggerTraining(
        @Query("epochs") epochs: Int,
        @Query("limit_data") limitData: Int
    ): Map<String, Any>

    @GET("train/status")
    suspend fun getTrainingStatus(): Map<String, Any>

    @GET("admin/users")
    suspend fun getUsers(): List<Map<String, Any>>

    @GET("admin/audit-logs")
    suspend fun getAuditLogs(): List<Map<String, Any>>

    @POST("admin/users/{user_id}/toggle-active")
    suspend fun toggleUserActive(@Path("user_id") userId: Int): Map<String, Any>

    @DELETE("admin/users/{user_id}")
    suspend fun deleteUser(@Path("user_id") userId: Int): Map<String, String>

    @GET("admin/system-metrics")
    suspend fun getSystemMetrics(): Map<String, Any>

    @POST("admin/promote/{user_id}")
    suspend fun promoteUser(
        @Path("user_id") userId: Int,
        @Query("role") role: String
    ): Map<String, String>

    @POST("analyze/oral-health")
    suspend fun analyzeOralHealth(): OralHealthReviewResponse

    @POST("clinical/notes")
    suspend fun saveClinicalNote(@Body request: NoteRequest): Map<String, String>

    @GET("clinical/notes/{file_name}")
    suspend fun getClinicalNotes(@Path("file_name") fileName: String): List<ClinicalNote>

    @GET("clinical/notes/patient/{patient_id}")
    suspend fun getPatientNotes(@Path("patient_id") patientId: String): List<ClinicalNote>

    @POST("patients/{patient_id}/toggle-status")
    suspend fun togglePatientStatus(@Path("patient_id") patientId: String): Map<String, Any>

    @GET("auth/user/status")
    suspend fun getUserPatientStatus(@Query("patient_id") patientId: String): Map<String, String>

    @POST("patients")
    suspend fun createPatient(@Body data: PatientCreate): Map<String, String>
}
