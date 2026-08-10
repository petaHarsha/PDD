package com.oralsurgeryai.app.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oralsurgeryai.app.data.CbctResponse
import com.oralsurgeryai.app.data.NetworkModule
import com.oralsurgeryai.app.ui.nerve.ClinicalMetrics
import com.oralsurgeryai.app.ui.nerve.NervePoint
import com.oralsurgeryai.app.ui.nerve.NerveTracingEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class CbctViewModel : ViewModel() {
    var cbctResponse by mutableStateOf<CbctResponse?>(null)
    var currentSliceIndex by mutableFloatStateOf(0f)
    
    var axialIndex by mutableStateOf(0)
    var coronalIndex by mutableStateOf(0)
    var sagittalIndex by mutableStateOf(0)

    var isLoading by mutableStateOf(false)
    var loadingProgress by mutableFloatStateOf(0f)
    var loadingStatus by mutableStateOf("Ready to Process")
    
    private val nerveEngine = NerveTracingEngine()
    var nervePath by mutableStateOf<List<NervePoint>>(emptyList())
    var clinicalMetrics by mutableStateOf<ClinicalMetrics?>(null)
    
    var viewMode by mutableStateOf("Analysis")
    var selectedPerspective by mutableStateOf("Axial")
    var activeLayer by mutableStateOf("Nerve")
    
    var errorMessage by mutableStateOf<String?>(null)

    fun uploadCbct(context: Context, uri: Uri, photoUri: Uri? = null, onComplete: (CbctResponse) -> Unit) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            var tempFile: File? = null
            var photoFile: File? = null
            try {
                loadingStatus = "Uploading Volumetric Data..."
                loadingProgress = 0.1f
                
                tempFile = uriToFile(context, uri, "upload_scan.nii.gz")
                val requestFile = tempFile.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)
                
                val photoPart = photoUri?.let { pUri ->
                    photoFile = uriToFile(context, pUri, "upload_photo.jpg")
                    val pReq = photoFile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("photo", photoFile!!.name, pReq)
                }

                val demoMode = "false".toRequestBody("text/plain".toMediaTypeOrNull())

                loadingStatus = "AI Engine: Initializing Penta-Planar Mapping..."
                loadingProgress = 0.3f
                delay(1000)
                
                val response = NetworkModule.apiService.uploadCbct(photoPart, body, demoMode)
                
                loadingStatus = "Constructing 3D Nerve Spline..."
                loadingProgress = 0.8f
                
                nervePath = nerveEngine.extractNervePath(response)
                clinicalMetrics = nerveEngine.calculateMetrics(nervePath)

                response.clinicalPerspectives.let { cp ->
                    val max = cp.maxIndices 
                    currentSliceIndex = (cp.initialIndices["axial"] ?: 0).toFloat() 
                    selectedPerspective = "Axial"
                    axialIndex = ((cp.initialIndices["axial"] ?: 0).toFloat() / (max["axial"] ?: 1).toFloat() * 39).toInt()
                    coronalIndex = ((cp.initialIndices["coronal"] ?: 0).toFloat() / (max["coronal"] ?: 1).toFloat() * 39).toInt()
                    sagittalIndex = ((cp.initialIndices["sagittal"] ?: 0).toFloat() / (max["sagittal"] ?: 1).toFloat() * 39).toInt()
                }
                
                cbctResponse = response
                onComplete(response)
            } catch (e: Exception) {
                errorMessage = "Pipeline Error: ${e.message}"
            } finally {
                tempFile?.delete()
                photoFile?.delete()
                isLoading = false
                loadingProgress = 1f
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri, fileName: String): File {
        val file = File(context.cacheDir, fileName)
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }
}


