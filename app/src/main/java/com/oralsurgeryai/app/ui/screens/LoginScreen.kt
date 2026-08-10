package com.oralsurgeryai.app.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oralsurgeryai.app.ui.components.ClinicalLogo
import com.oralsurgeryai.app.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, viewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current
    var isPasswordVisible by remember { mutableStateOf(false) }
    
    // Auto-Discover Server on startup
    LaunchedEffect(Unit) {
        val discoveredIp = com.oralsurgeryai.app.data.NetworkDiscovery.discoverServerIp()
        discoveredIp?.let {
            com.oralsurgeryai.app.data.NetworkModule.updateIp(it)
            Toast.makeText(context, "Server Detected: $it", Toast.LENGTH_SHORT).show()
        }
    }
    
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.errorMessage = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // Header Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ClinicalLogo()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Oral Surgery AI",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
            ) {
                Text(
                    text = "AI-POWERED DIAGNOSTICS",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
        
        // Main Card
        Surface(
            modifier = Modifier.fillMaxWidth().widthIn(max = 440.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                if (viewModel.isRegisterMode) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (!viewModel.isVerificationMode) {
                            Text(
                                "CREATE YOUR ACCOUNT",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("CLINICAL EMAIL", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                OutlinedTextField(
                                    value = viewModel.email,
                                    onValueChange = { viewModel.email = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                                    )
                                )
                            }
                            Button(
                                onClick = { viewModel.requestRegistrationOtp() },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = CircleShape,
                                enabled = !viewModel.isLoading
                            ) {
                                if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                                else Text("GET VERIFICATION CODE", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(
                                "VERIFY YOUR EMAIL",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "OTP sent to: ${viewModel.email}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("VERIFICATION CODE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                OutlinedTextField(
                                    value = viewModel.otp,
                                    onValueChange = { viewModel.otp = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    placeholder = { Text("6-digit code") }
                                )
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("FULL NAME", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                OutlinedTextField(
                                    value = viewModel.fullName,
                                    onValueChange = { viewModel.fullName = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                                    )
                                )
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("SURGICAL PORTAL PASSWORD", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                OutlinedTextField(
                                    value = viewModel.password,
                                    onValueChange = { viewModel.password = it },
                                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    trailingIcon = {
                                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                            Icon(
                                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                                    )
                                )
                            }
                            Button(
                                onClick = { viewModel.register() },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = CircleShape,
                                enabled = !viewModel.isLoading
                            ) {
                                if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                                else Text("COMPLETE REGISTRATION", fontWeight = FontWeight.Bold)
                            }
                            TextButton(onClick = { viewModel.isVerificationMode = false }) {
                                Text("Change Email", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            }
                        }
                    }
                } else
if (!viewModel.isForgotPasswordMode) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("CLINICAL EMAIL", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            OutlinedTextField(
                                value = viewModel.email,
                                onValueChange = { viewModel.email = it },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                                shape = RoundedCornerShape(20.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                                )
                            )
                        }
                        
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("SURGICAL PORTAL PASSWORD", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            OutlinedTextField(
                                value = viewModel.password,
                                onValueChange = { viewModel.password = it },
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                                )
                            )
                        }
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = true, onCheckedChange = {})
                                Text("Remember me", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            TextButton(onClick = { viewModel.isForgotPasswordMode = true }) {
                                Text("Forgot password?", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        
                        Button(
                            onClick = { viewModel.login(onLoginSuccess) },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            enabled = !viewModel.isLoading
                        ) {
                            if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                            else {
                                Text("Sign In", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Submit", modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                } else {
                    // Password Recovery UI
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Password Recovery", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Enter your clinical email to receive a recovery code.", style = MaterialTheme.typography.bodySmall)
                        
                        OutlinedTextField(
                            value = viewModel.email,
                            onValueChange = { viewModel.email = it },
                            label = { Text("Clinical Email") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            enabled = viewModel.otp.isEmpty()
                        )
                        
                        if (viewModel.otp.isNotEmpty()) {
                            OutlinedTextField(
                                value = if (viewModel.otp == "PENDING") "" else viewModel.otp,
                                onValueChange = { viewModel.otp = it },
                                label = { Text("Recovery Code") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp)
                            )
                            OutlinedTextField(
                                value = viewModel.password,
                                onValueChange = { viewModel.password = it },
                                label = { Text("New Password") },
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp)
                            )
                        }

                        Button(
                            onClick = {
                                if (viewModel.otp.isEmpty()) {
                                    viewModel.forgotPassword()
                                } else {
                                    viewModel.resetPassword()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = CircleShape,
                            enabled = !viewModel.isLoading
                        ) {
                            if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                            else Text(if (viewModel.otp.isEmpty()) "SEND RECOVERY CODE" else "RESET PASSWORD", fontWeight = FontWeight.Bold)
                        }

                        TextButton(
                            onClick = { 
                                viewModel.cancelActiveRequest()
                                viewModel.isForgotPasswordMode = false
                                viewModel.otp = "" 
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back to Login", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                    }
                }

                TextButton(
                    onClick = { 
                        viewModel.cancelActiveRequest()
                        viewModel.isRegisterMode = !viewModel.isRegisterMode 
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(if (viewModel.isRegisterMode) "Already have an account? Sign In" else "Don't have an account? Join Now")
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Footer
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 24.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("HIPAA Compliance", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Privacy Policy", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "© 2026 ORAL SURGERY AI • MEDICAL GRADE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
