package com.oralsurgeryai.app.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object UserSession {
    var user: UserInfo? by mutableStateOf(null)
    
    val userName: String
        get() = user?.name ?: "Guest Clinician"
        
    val userRole: String
        get() = user?.role ?: "Surgeon"
        
    val userEmail: String
        get() = user?.email ?: ""
        
    val patientId: String?
        get() = user?.patient_id

    fun clear() {
        user = null
    }
}
