package com.kanawish.sample.hello.view

import android.Manifest
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import androidx.camera.core.ExperimentalLensFacing
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@androidx.annotation.OptIn(ExperimentalLensFacing::class)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraDiagnostics(skipDiagnostics: Boolean = false) {
    var diagnosticsText by remember { mutableStateOf("Waiting on diagnostics...") }

    // This effect populates the diagnosticsText. It will only run once, etc.
    if(!skipDiagnostics) {
        val context = LocalContext.current
        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
        LaunchedEffect(Unit) {
            val sb = StringBuilder()

            // Permission Status
            sb.appendLine("📱 Camera Diagnostics")
            sb.appendLine("===================")
            sb.appendLine("Permission Status: ${if (cameraPermissionState.status.isGranted) "✅ CAMERA permission granted" else "❌ CAMERA permission NOT granted"}")
            sb.appendLine()

            if (cameraPermissionState.status.isGranted) {
                try {
                    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    val cameraIds = cameraManager.cameraIdList

                    // Camera2 API Information
                    sb.appendLine("📸 Camera2 API Information")
                    sb.appendLine("------------------------")
                    sb.appendLine("Number of cameras: ${cameraIds.size}")

                    cameraIds.forEach { cameraId ->
                        val characteristics = cameraManager.getCameraCharacteristics(cameraId)

                        // Basic Camera Info
                        sb.appendLine("\nCamera ID: $cameraId")

                        // Facing Direction
                        val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                        val facingText = when (facing) {
                            CameraCharacteristics.LENS_FACING_FRONT -> "Front"
                            CameraCharacteristics.LENS_FACING_BACK -> "Back"
                            CameraCharacteristics.LENS_FACING_EXTERNAL -> "External"
                            else -> "Unknown"
                        }
                        sb.appendLine("Facing: $facingText")

                        // Hardware Level
                        val hardwareLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                        val hardwareLevelText = when (hardwareLevel) {
                            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY -> "LEGACY"
                            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED -> "LIMITED"
                            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL -> "FULL"
                            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3 -> "LEVEL_3"
                            else -> "UNKNOWN"
                        }
                        sb.appendLine("Hardware Level: $hardwareLevelText")

                        // Sensor Orientation
                        val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
                        sb.appendLine("Sensor Orientation: ${sensorOrientation}°")

                        // Physical Characteristics
                        sb.appendLine("\nPhysical Characteristics:")
                        val physicalSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)
                        if (physicalSize != null) {
                            sb.appendLine("Sensor Physical Size: ${physicalSize.width}mm x ${physicalSize.height}mm")
                        }

                        val pixelArraySize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)
                        if (pixelArraySize != null) {
                            sb.appendLine("Pixel Array Size: ${pixelArraySize.width}x${pixelArraySize.height}")
                        }

                        val activeArraySize = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE)
                        if (activeArraySize != null) {
                            sb.appendLine("Active Array Size: ${activeArraySize.width()}x${activeArraySize.height()}")
                        }

                        // Flash Information
                        /*
                                            val flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
                                            sb.appendLine("\nFlash Information:")
                                            sb.appendLine("Flash Available: ${if (flashAvailable) "Yes" else "No"}")
                                            if (flashAvailable) {
                                                val flashChargeTime = characteristics.get(CameraCharacteristics.FLASH_INFO_CHARGE_DURATION)
                                                if (flashChargeTime != null) {
                                                    sb.appendLine("Flash Charge Time: ${flashChargeTime}ms")
                                                }
                                            }
                        */

                        // Autofocus Capabilities
                        val afModes = characteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES)
                        if (afModes != null) {
                            sb.appendLine("\nAutofocus Capabilities:")
                            sb.appendLine("Available Modes:")
                            afModes.forEach { mode ->
                                val modeText = when (mode) {
                                    CameraMetadata.CONTROL_AF_MODE_OFF -> "OFF"
                                    CameraMetadata.CONTROL_AF_MODE_AUTO -> "AUTO"
                                    CameraMetadata.CONTROL_AF_MODE_MACRO -> "MACRO"
                                    CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO -> "CONTINUOUS_VIDEO"
                                    CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE -> "CONTINUOUS_PICTURE"
                                    CameraMetadata.CONTROL_AF_MODE_EDOF -> "EDOF"
                                    else -> "UNKNOWN"
                                }
                                sb.appendLine("- $modeText")
                            }

                            // AF Regions
                            val maxAfRegions = characteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)
                            if (maxAfRegions != null) {
                                sb.appendLine("Maximum AF Regions: $maxAfRegions")
                            }
                        }

                        // Focus Distance
                        val focusDistance = characteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE)
                        if (focusDistance != null) {
                            sb.appendLine("Minimum Focus Distance: $focusDistance")
                        }

                        // Focal Length
                        val focalLengths = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
                        if (focalLengths != null) {
                            sb.appendLine("Available Focal Lengths: ${focalLengths.joinToString(", ")}")
                        }

                        // Optical Stabilization
                        val oisModes = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION)
                        if (oisModes != null) {
                            sb.appendLine("\nOptical Stabilization:")
                            oisModes.forEach { mode ->
                                val modeText = when (mode) {
                                    CameraMetadata.LENS_OPTICAL_STABILIZATION_MODE_OFF -> "OFF"
                                    CameraMetadata.LENS_OPTICAL_STABILIZATION_MODE_ON -> "ON"
                                    else -> "UNKNOWN"
                                }
                                sb.appendLine("- $modeText")
                            }
                        }

                        // Resolution and Format Support
                        val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        if (map != null) {
                            sb.appendLine("\nOutput Configuration:")
                            // JPEG Sizes
                            sb.appendLine("JPEG Output Sizes:")
                            map.getOutputSizes(android.graphics.ImageFormat.JPEG).forEach { size ->
                                sb.appendLine("- ${size.width}x${size.height}")
                            }

                            // YUV Sizes
                            sb.appendLine("\nYUV Output Sizes:")
                            map.getOutputSizes(android.graphics.ImageFormat.YUV_420_888).forEach { size ->
                                sb.appendLine("- ${size.width}x${size.height}")
                            }

                            // Input/Output format map
                            sb.appendLine("\nInput/Output Format Map:")
                            map.inputFormats.forEach { format ->
                                val formatName = when (format) {
                                    // ImageFormat constants
                                    android.graphics.ImageFormat.JPEG -> "JPEG"
                                    android.graphics.ImageFormat.YUV_420_888 -> "YUV_420_888"
                                    android.graphics.ImageFormat.YUV_422_888 -> "YUV_422_888"
                                    android.graphics.ImageFormat.YUV_444_888 -> "YUV_444_888"
                                    android.graphics.ImageFormat.FLEX_RGB_888 -> "FLEX_RGB_888"
                                    android.graphics.ImageFormat.FLEX_RGBA_8888 -> "FLEX_RGBA_8888"
                                    android.graphics.ImageFormat.RAW_SENSOR -> "RAW_SENSOR"
                                    android.graphics.ImageFormat.RAW_PRIVATE -> "RAW_PRIVATE"
                                    android.graphics.ImageFormat.DEPTH16 -> "DEPTH16"
                                    android.graphics.ImageFormat.DEPTH_POINT_CLOUD -> "DEPTH_POINT_CLOUD"
                                    // PixelFormat constants
                                    android.graphics.PixelFormat.RGBA_8888 -> "RGBA_8888"
                                    android.graphics.PixelFormat.RGBX_8888 -> "RGBX_8888"
                                    android.graphics.PixelFormat.RGB_888 -> "RGB_888"
                                    android.graphics.PixelFormat.RGB_565 -> "RGB_565"
                                    else -> "Unknown format ($format)"
                                }
                                sb.appendLine("Input: $formatName")
                            }
                            map.outputFormats.forEach { format ->
                                val formatName = when (format) {
                                    // ImageFormat constants
                                    android.graphics.ImageFormat.JPEG -> "JPEG"
                                    android.graphics.ImageFormat.YUV_420_888 -> "YUV_420_888"
                                    android.graphics.ImageFormat.YUV_422_888 -> "YUV_422_888"
                                    android.graphics.ImageFormat.YUV_444_888 -> "YUV_444_888"
                                    android.graphics.ImageFormat.FLEX_RGB_888 -> "FLEX_RGB_888"
                                    android.graphics.ImageFormat.FLEX_RGBA_8888 -> "FLEX_RGBA_8888"
                                    android.graphics.ImageFormat.RAW_SENSOR -> "RAW_SENSOR"
                                    android.graphics.ImageFormat.RAW_PRIVATE -> "RAW_PRIVATE"
                                    android.graphics.ImageFormat.DEPTH16 -> "DEPTH16"
                                    android.graphics.ImageFormat.DEPTH_POINT_CLOUD -> "DEPTH_POINT_CLOUD"
                                    // PixelFormat constants
                                    android.graphics.PixelFormat.RGBA_8888 -> "RGBA_8888"
                                    android.graphics.PixelFormat.RGBX_8888 -> "RGBX_8888"
                                    android.graphics.PixelFormat.RGB_888 -> "RGB_888"
                                    android.graphics.PixelFormat.RGB_565 -> "RGB_565"
                                    else -> "Unknown format ($format)"
                                }
                                sb.appendLine("Output: $formatName")
                            }
                        }

                        // Performance Characteristics
                        sb.appendLine("\nPerformance Characteristics:")
                        val maxPreviewFps = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)
                        if (maxPreviewFps != null) {
                            sb.appendLine("Available FPS Ranges:")
                            maxPreviewFps.forEach { range ->
                                sb.appendLine("- ${range.lower} to ${range.upper} FPS")
                            }
                        }

                        val maxJpegSize = characteristics.get(CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES)
                        if (maxJpegSize != null) {
                            sb.appendLine("Available JPEG Sizes:")
                            maxJpegSize.forEach { size ->
                                sb.appendLine("- ${size.width}x${size.height}")
                            }
                        }
                    }

                    // CameraX Provider Status
                    sb.appendLine("\n📸 CameraX Provider Status")
                    sb.appendLine("------------------------")
                    try {
                        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
                        sb.appendLine("CameraX Provider: ✅ Available")

                        // Check available cameras through CameraX
                        val availableCameras = cameraProvider.availableCameraInfos
                        sb.appendLine("Number of cameras (CameraX): ${availableCameras.size}")

                        availableCameras.forEach { cameraInfo ->
                            sb.appendLine("\nCameraX Camera Info:")
                            sb.appendLine("Lens Facing: ${cameraInfo.lensFacing}")
                            sb.appendLine("Sensor Rotation: ${cameraInfo.sensorRotationDegrees}°")
                            sb.appendLine("Has Flash: ${cameraInfo.hasFlashUnit()}")

                            // Additional CameraX Info
                            sb.appendLine("Torch State: ${cameraInfo.torchState}")
                            sb.appendLine("Zoom Ratio: ${cameraInfo.zoomState?.value ?: "N/A"}")
                            sb.appendLine("Exposure State: ${cameraInfo.exposureState?.exposureCompensationIndex ?: "N/A"}")
                        }
                    } catch (e: Exception) {
                        sb.appendLine("CameraX Provider: ❌ Error: ${e.message}")
                        sb.appendLine("Stack trace: ${e.stackTraceToString()}")
                    }
                } catch (e: Exception) {
                    sb.appendLine("❌ Error getting camera information:")
                    sb.appendLine(e.message)
                    sb.appendLine("Stack trace: ${e.stackTraceToString()}")
                }
            }

            diagnosticsText = sb.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = diagnosticsText)
    }
} 