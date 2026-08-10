package com.oralsurgeryai.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.oralsurgeryai.app.data.NetworkModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TrainingScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var epochs by remember { mutableStateOf("5") }
    var limitData by remember { mutableStateOf("10") }
    var isTrainingRunning by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("Status: Checking...") }

    // Poll for training status every 5 seconds
    LaunchedEffect(Unit) {
        while(true) {
            try {
                val status = NetworkModule.apiService.getTrainingStatus()
                isTrainingRunning = status["status"] == "running"
                statusMessage = "Status: ${status["status"]}"
            } catch (e: Exception) {
                statusMessage = "Status: Offline"
            }
            delay(5000)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("AI Model Training Control", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = epochs,
            onValueChange = { epochs = it },
            label = { Text("Epochs") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isTrainingRunning
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = limitData,
            onValueChange = { limitData = it },
            label = { Text("Data Limit (Images)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isTrainingRunning
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                scope.launch {
                    try {
                        NetworkModule.apiService.triggerTraining(
                            epochs = epochs.toIntOrNull() ?: 5,
                            limitData = limitData.toIntOrNull() ?: 10
                        )
                        Toast.makeText(context, "Training Started", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isTrainingRunning
        ) {
            if (isTrainingRunning) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Training in Progress...")
            } else {
                Text("Start Training Run")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(statusMessage, style = MaterialTheme.typography.titleMedium)
                if (isTrainingRunning) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                    Text(
                        "The model is currently learning from the $limitData files. You can close the app; training will continue on the server.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
