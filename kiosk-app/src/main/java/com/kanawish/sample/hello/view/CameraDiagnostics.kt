package com.kanawish.sample.hello.view

import android.Manifest
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalLensFacing
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@androidx.annotation.OptIn(ExperimentalLensFacing::class)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraDiagnostics() {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    var cameraInfo by remember { mutableStateOf<String>("Checking cameras...") }
    
    LaunchedEffect(Unit) {
        try {
            // First, check camera permission
            cameraInfo = buildString {
                appendLine("📱 Device Camera Diagnostics")
                appendLine("---------------------------")
                appendLine("Permission Status: ${if (cameraPermissionState.status.isGranted) "✅ CAMERA permission granted" else "❌ CAMERA permission NOT granted"}")
                appendLine()

                // Check Camera2 API level info first
                val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                val cameraIds = cameraManager.cameraIdList
                
                appendLine("📸 Camera2 API Information:")
                appendLine("Number of cameras: ${cameraIds.size}")
                
                cameraIds.forEach { id ->
                    val characteristics = cameraManager.getCameraCharacteristics(id)
                    val facing = when(characteristics.get(CameraCharacteristics.LENS_FACING)) {
                        CameraCharacteristics.LENS_FACING_FRONT -> "Front"
                        CameraCharacteristics.LENS_FACING_BACK -> "Back"
                        CameraCharacteristics.LENS_FACING_EXTERNAL -> "External"
                        else -> "Unknown"
                    }
                    
                    val hardwareLevel = when(characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)) {
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY -> "LEGACY"
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED -> "LIMITED"
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL -> "FULL"
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3 -> "LEVEL_3"
                        else -> "UNKNOWN"
                    }
                    
                    appendLine("\nCamera ID: $id")
                    appendLine("- Facing: $facing")
                    appendLine("- Hardware Level: $hardwareLevel")
                    appendLine("- Flash Available: ${characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true}")
                }
                
                appendLine("\n")
                appendLine("🔄 CameraX Provider Status:")
                
                // Then check CameraX availability
                try {
                    val provider = getCameraProvider(context)
                    val cameras = provider.availableCameraInfos
                    
                    if (cameras.isEmpty()) {
                        appendLine("❌ No cameras available through CameraX")
                    } else {
                        appendLine("✅ CameraX initialized successfully")
                        appendLine("Number of available cameras: ${cameras.size}")
                        
                        cameras.forEachIndexed { index, cameraInfo ->
                            val lensFacing = when(cameraInfo.lensFacing) {
                                CameraSelector.LENS_FACING_FRONT -> "Front"
                                CameraSelector.LENS_FACING_BACK -> "Back"
                                CameraSelector.LENS_FACING_EXTERNAL -> "External"
                                else -> "Unknown"
                            }
                            appendLine("\nCameraX Camera $index:")
                            appendLine("- Facing: $lensFacing")
                            appendLine("- Sensor Rotation: ${cameraInfo.sensorRotationDegrees}°")
                        }
                    }
                } catch (e: Exception) {
                    appendLine("❌ Failed to initialize CameraX: ${e.message}")
                }
            }
        } catch (e: Exception) {
            cameraInfo = buildString {
                appendLine("❌ Error accessing camera system:")
                appendLine(e.message ?: "Unknown error")
                appendLine("\nStack trace:")
                e.stackTrace.take(3).forEach { appendLine(it.toString()) }
            }
            Timber.e(e, "Failed to check camera availability")
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = cameraInfo)
    }
}

private suspend fun getCameraProvider(context: Context): ProcessCameraProvider {
    return suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(context).also { future ->
            future.addListener({
                continuation.resume(future.get())
            }, ContextCompat.getMainExecutor(context))
        }
    }
} 