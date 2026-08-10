package com.oralsurgeryai.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oralsurgeryai.app.data.LoginRequest
import com.oralsurgeryai.app.data.NetworkModule
import com.oralsurgeryai.app.data.RegisterRequest
import com.oralsurgeryai.app.data.UserSession
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Date
import org.json.JSONObject
import kotlinx.coroutines.CancellationException

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var fullName by mutableStateOf("")
    var otp by mutableStateOf("")
    
    var isRegisterMode by mutableStateOf(false)
    var isForgotPasswordMode by mutableStateOf(false)
    var isVerificationMode by mutableStateOf(false)
    
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private var currentJob: Job? = null

    fun cancelActiveRequest() {
        currentJob?.cancel()
        currentJob = null
        isLoading = false
    }

    fun login(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please enter both email and password"
            return
        }

        cancelActiveRequest()
        isLoading = true
        errorMessage = null
        currentJob = viewModelScope.launch {
            try {
                val response = NetworkModule.apiService.login(LoginRequest(email, password))
                if (response.status == "success") {
                    UserSession.user = response.user
                    onSuccess()
                } else {
                    errorMessage = "Login failed: Unauthorized access"
                }
            } catch (e: HttpException) {
                errorMessage = when (e.code()) {
                    401 -> "Invalid credentials. Please check your email and password."
                    404 -> "Authentication service not found."
                    500 -> "Server error. Please try again later."
                    else -> parseErrorMessage(e) ?: "Network error (${e.code()})"
                }
            } catch (e: CancellationException) {
                // Ignore cancellation
            } catch (e: Exception) {
                errorMessage = e.message ?: "Check your internet connection"
            } finally {
                isLoading = false
            }
        }
    }

    private fun parseErrorMessage(e: HttpException): String? {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(errorBody ?: "{}")
            val detail = jsonObject.optString("detail", "")
            if (detail.isNotEmpty()) detail else jsonObject.optString("message", "")
        } catch (ex: Exception) {
            null
        }
    }

    fun register() {
        if (email.isBlank() || password.isBlank() || fullName.isBlank() || otp.isBlank()) {
            errorMessage = "Please fill in all fields including the verification code"
            return
        }

        cancelActiveRequest()
        isLoading = true
        errorMessage = null
        currentJob = viewModelScope.launch {
            try {
                NetworkModule.apiService.register(RegisterRequest(email, password, fullName, otp))
                isRegisterMode = false
                isVerificationMode = false
                otp = ""
                errorMessage = "Registration Successful. Please Sign In."
            } catch (e: CancellationException) {
                // Ignore
            } catch (e: Exception) {
                errorMessage = e.message ?: "Registration failed"
            } finally {
                isLoading = false
            }
        }
    }

    fun requestRegistrationOtp() {
        if (email.isBlank()) {
            errorMessage = "Please enter your email to receive a code"
            return
        }

        cancelActiveRequest()
        isLoading = true
        errorMessage = null
        currentJob = viewModelScope.launch {
            try {
                NetworkModule.apiService.requestRegistrationOtp(mapOf("email" to email))
                isVerificationMode = true
                errorMessage = "Verification code sent to $email"
            } catch (e: CancellationException) {
                // Ignore
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to send code"
            } finally {
                isLoading = false
            }
        }
    }

    fun verifyRegistration() {
        // This is now handled within register()
    }

    fun forgotPassword() {
        cancelActiveRequest()
        isLoading = true
        errorMessage = null
        currentJob = viewModelScope.launch {
            try {
                NetworkModule.apiService.forgotPassword(mapOf("email" to email))
                otp = "PENDING"
            } catch (e: CancellationException) {
                // Ignore
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun resetPassword() {
        cancelActiveRequest()
        isLoading = true
        errorMessage = null
        currentJob = viewModelScope.launch {
            try {
                NetworkModule.apiService.resetPassword(mapOf(
                    "email" to email,
                    "otp" to otp,
                    "new_password" to password
                ))
                isForgotPasswordMode = false
                otp = ""
                errorMessage = "Password Updated Successfully"
            } catch (e: CancellationException) {
                // Ignore
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
