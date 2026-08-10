package com.oralsurgeryai.app.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oralsurgeryai.app.data.CbctResponse
import com.oralsurgeryai.app.ui.theme.*
import com.oralsurgeryai.app.ui.viewmodel.CbctViewModel
import com.oralsurgeryai.app.ui.nerve.NervePathRenderer
import com.oralsurgeryai.app.ui.nerve.NerveViewMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CbctViewerScreen(
    onAnalysisComplete: (CbctResponse) -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToNerve: () -> Unit = {},
    onNavigateToTumor: () -> Unit = {},
    viewModel: CbctViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedPhotoUri = uri
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
            .background(if (viewModel.cbctResponse == null) MaterialTheme.colorScheme.background else Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        if (viewModel.cbctResponse == null) {
            // UPLOAD HUB
            Column(modifier = Modifier.padding(24.dp)) {
                Text("SCAN UPLOAD HUB", style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(2.dp, Secondary.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                progress = viewModel.loadingProgress,
                                modifier = Modifier.size(80.dp),
                                color = Secondary,
                                strokeWidth = 6.dp,
                                trackColor = SurfaceContainerLow
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(viewModel.loadingStatus, fontWeight = FontWeight.Bold, color = Primary)
                            Text("Please do not close the surgical portal", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                        } else {
                            // 1. PRIMARY NIFTI PICKER
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (selectedFileUri != null) Secondary.copy(alpha = 0.05f) else SurfaceContainerLow)
                                    .clickable { filePickerLauncher.launch("*/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 16.dp)) {
                                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = if (selectedFileUri != null) Secondary else OnSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = selectedFileUri?.path?.split("/")?.last() ?: "SELECT NIFTI VOLUME", 
                                        fontSize = 12.sp, 
                                        fontWeight = FontWeight.Black, 
                                        color = if (selectedFileUri != null) Primary else OnSurfaceVariant,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 2. OPTIONAL PHOTO PICKER
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (selectedPhotoUri != null) Primary.copy(alpha = 0.05f) else SurfaceContainerLow)
                                    .clickable { photoPickerLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = if (selectedPhotoUri != null) Primary else OnSurfaceVariant, modifier = Modifier.size(20.dp))
                                    Text(selectedPhotoUri?.path?.split("/")?.last() ?: "ADD CLINICAL PHOTO (OPTIONAL)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (selectedPhotoUri != null) Primary else OnSurfaceVariant)
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = { selectedFileUri?.let { viewModel.uploadCbct(context, it, selectedPhotoUri, onAnalysisComplete) } },
                                modifier = Modifier.fillMaxWidth().height(64.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                                enabled = selectedFileUri != null
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("INITIALIZE AI ANALYSIS", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            }
                        }
                    }
                }
            }
        } else {
            // ANALYSIS WORKSPACE (Dual Mode)
            WorkspaceContent(viewModel, onNavigateToNerve, onNavigateToTumor)
        }
    }
}

@Composable
fun WorkspaceContent(viewModel: CbctViewModel, onNavigateToNerve: () -> Unit, onNavigateToTumor: () -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Mode Selector (Analysis vs Volume BETA)
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(SurfaceContainerHigh).padding(4.dp)
        ) {
            val modes = listOf("Analysis", "Volume BETA")
            modes.forEach { mode ->
                val active = (viewModel.viewMode == mode) || (viewModel.viewMode == "Volumetric" && mode == "Analysis")
                Surface(
                    modifier = Modifier.weight(1f).clickable { viewModel.viewMode = mode },
                    color = if (active) Color.White else Color.Transparent,
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = if (active) 2.dp else 0.dp
                ) {
                    Text(mode, modifier = Modifier.padding(8.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontSize = 10.sp, fontWeight = FontWeight.Black, color = if (active) Secondary else OnSurfaceVariant)
                }
            }
        }

        if (viewModel.viewMode == "Volume BETA") {
            // VOLVIZ QUAD VIEW (Mobile Implementation)
            Column(modifier = Modifier.padding(8.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MPRCard("Axial", viewModel.cbctResponse?.clinicalPerspectives?.axialStack?.get(viewModel.axialIndex), viewModel.axialIndex, Modifier.weight(1f)) { viewModel.axialIndex = it }
                    MPRCard("Sagittal", viewModel.cbctResponse?.clinicalPerspectives?.sagittalStack?.get(viewModel.sagittalIndex), viewModel.sagittalIndex, Modifier.weight(1f)) { viewModel.sagittalIndex = it }
                }
                Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MPRCard("Coronal", viewModel.cbctResponse?.clinicalPerspectives?.coronalStack?.get(viewModel.coronalIndex), viewModel.coronalIndex, Modifier.weight(1f)) { viewModel.coronalIndex = it }
                    Surface(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(16.dp)), color = Color(0xFF111111)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.ViewInAr, contentDescription = null, tint = Secondary.copy(alpha = 0.2f), modifier = Modifier.size(64.dp))
                            Text("3D SPACE", color = Color.White.copy(alpha = 0.4f), fontWeight = FontWeight.Black, fontSize = 10.sp, modifier = Modifier.align(Alignment.TopStart).padding(12.dp))
                        }
                    }
                }
            }
        } else {
            // PRIMARY ANALYSIS VIEW (Original focus)
            Column(modifier = Modifier.padding(16.dp)) {
                Text("RECONSTRUCTION: AXIAL", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(24.dp)).background(Color(0xFF050505)).pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale *= zoom
                        offset += pan
                    }
                }) {
                    // Raw base layer
                    CommonUi.Base64Image(
                        base64 = viewModel.cbctResponse?.slicesRaw?.get(viewModel.currentSliceIndex.toInt()) ?: "",
                        modifier = Modifier.fillMaxSize().graphicsLayer(scaleX = scale, scaleY = scale, translationX = offset.x, translationY = offset.y)
                    )
                    
                    // Nerve Layer (Yellow Tracing)
                    if (viewModel.activeLayer == "Nerve") {
                        CommonUi.Base64Image(
                            base64 = viewModel.cbctResponse?.slicesRight?.get(viewModel.currentSliceIndex.toInt()) ?: "",
                            modifier = Modifier.fillMaxSize().graphicsLayer(scaleX = scale, scaleY = scale, translationX = offset.x, translationY = offset.y),
                            alpha = 0.8f
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Slider(
                    value = viewModel.currentSliceIndex,
                    onValueChange = { viewModel.currentSliceIndex = it },
                    valueRange = 0f..(viewModel.cbctResponse?.slicesRaw?.size?.minus(1)?.toFloat() ?: 1f),
                    colors = SliderDefaults.colors(thumbColor = Secondary, activeTrackColor = Secondary)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Navigation Bar
        Surface(modifier = Modifier.fillMaxWidth(), color = Color.Black, border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))) {
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                ActionButton(Icons.Default.Insights, "NERVE") { onNavigateToNerve() }
                ActionButton(Icons.Default.Warning, "TUMOR") { onNavigateToTumor() }
                ActionButton(Icons.Default.Description, "REPORT") { /* TODO */ }
            }
        }
    }
}

@Composable
fun MPRCard(title: String, layer: com.oralsurgeryai.app.data.PerspectiveLayer?, index: Int, modifier: Modifier = Modifier, onIndexChange: (Int) -> Unit) {
    Surface(modifier = modifier.fillMaxHeight(), color = Color(0xFF050505), shape = RoundedCornerShape(16.dp)) {
        Box {
            layer?.let {
                CommonUi.Base64Image(base64 = it.raw, modifier = Modifier.fillMaxSize())
                CommonUi.Base64Image(base64 = it.nerve, modifier = Modifier.fillMaxSize(), alpha = 0.8f)
            }
            Text(title, color = Color.White.copy(alpha = 0.4f), fontWeight = FontWeight.Black, fontSize = 8.sp, modifier = Modifier.align(Alignment.TopStart).padding(8.dp))
            Slider(
                value = index.toFloat(),
                onValueChange = { onIndexChange(it.toInt()) },
                valueRange = 0f..39f,
                modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 8.dp),
                colors = SliderDefaults.colors(thumbColor = Secondary, activeTrackColor = Secondary)
            )
        }
    }
}

@Composable
fun ActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        Text(label, color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp, fontWeight = FontWeight.Bold)
    }
}
