package com.kanawish.sample.hello.view

import android.Manifest
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalLensFacing
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.kanawish.sample.hello.navigation.MainNav
import com.kanawish.sample.hello.navigation.TypeRoute
import org.koin.compose.koinInject
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@androidx.annotation.OptIn(ExperimentalLensFacing::class)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraView(
    mainNav: MainNav = koinInject(),
    onQrCodeDetected: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val scanner = remember { BarcodeScanning.getClient() }
    val scope = rememberCoroutineScope()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var showCameraSelector by remember { mutableStateOf(true) }
    var selectedCameraId by remember { mutableStateOf<String?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    // Request camera permission
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    // Get available cameras
    val availableCameras = remember {
        if (cameraPermissionState.status.isGranted) {
            try {
                val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                cameraManager.cameraIdList.map { cameraId ->
                    val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                    val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                    val lensFacing = when (facing) {
                        CameraCharacteristics.LENS_FACING_FRONT -> CameraSelector.LENS_FACING_FRONT
                        CameraCharacteristics.LENS_FACING_BACK -> CameraSelector.LENS_FACING_BACK
                        CameraCharacteristics.LENS_FACING_EXTERNAL -> CameraSelector.LENS_FACING_EXTERNAL
                        else -> null
                    }
                    Triple(cameraId, facing, lensFacing)
                }
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    // Camera selection dialog
    if (showCameraSelector && availableCameras.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { showCameraSelector = false },
            title = { Text("Select Camera") },
            text = {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column {
                        availableCameras.forEachIndexed { index, (cameraId, facing, _) ->
                            val facingText = when (facing) {
                                CameraCharacteristics.LENS_FACING_FRONT -> "Front"
                                CameraCharacteristics.LENS_FACING_BACK -> "Back"
                                CameraCharacteristics.LENS_FACING_EXTERNAL -> "External"
                                else -> "Unknown"
                            }
                            Button(
                                onClick = {
                                    selectedCameraId = cameraId
                                    showCameraSelector = false
                                },
                                modifier = Modifier
                                    .padding(top = (index * 8).dp)
                            ) {
                                Text("$facingText Camera ($cameraId)")
                            }
                        }
                    }
                }
            },
            confirmButton = { },
            dismissButton = {
                Button(onClick = { showCameraSelector = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Camera preview
    if (cameraPermissionState.status.isGranted && selectedCameraId != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Back button
            androidx.compose.material3.IconButton(
                onClick = { mainNav.navBack(TypeRoute.MainRoute)},
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            AndroidView<PreviewView>(
                factory = { context ->
                    PreviewView(context).apply {
                        previewView = this
                        layoutParams = android.view.ViewGroup.LayoutParams(
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { view ->
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().apply {
                            surfaceProvider = view.surfaceProvider
                        }

                        // Add QR code analysis
                        val imageAnalyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .apply {
                                setAnalyzer(cameraExecutor) { imageProxy ->
                                    val mediaImage = imageProxy.image
                                    if (mediaImage != null) {
                                        val image = InputImage.fromMediaImage(
                                            mediaImage,
                                            imageProxy.imageInfo.rotationDegrees
                                        )

                                        scanner.process(image)
                                            .addOnSuccessListener { barcodes ->
                                                barcodes.firstOrNull()?.let { barcode ->
                                                    if (barcode.format == Barcode.FORMAT_QR_CODE) {
                                                        barcode.rawValue?.let { qrCode ->
                                                            onQrCodeDetected(qrCode)
                                                        }
                                                    }
                                                }
                                            }
                                            .addOnCompleteListener { imageProxy.close() }
                                    } else {
                                        imageProxy.close()
                                    }
                                }
                            }

                        val cameraSelector = CameraSelector.Builder().build()

                        try {
                            cameraProvider?.unbindAll()
                            cameraProvider?.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalyzer
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(context))
                }
            )
        }
    } else if (!cameraPermissionState.status.isGranted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera permission is required")
        }
    }
}

@Composable
private fun Camera.diagnostics(
    cameraExecutor: ExecutorService?,
    previewView: PreviewView?,
    camera: Camera
) {
    cameraInfo.exposureState.let { exposureState ->
        if (exposureState.isExposureCompensationSupported) {
            cameraControl.setExposureCompensationIndex(exposureState.exposureCompensationRange.upper)
                .addListener(
                    { Timber.d("Max exposure set attempt completed") },
                    cameraExecutor
                )
        }
    }
    // Try to set zoom to 2x - should be noticeable if it works
    cameraControl.setZoomRatio(2.0f)
        .addListener(
            { Timber.d("Zoom attempt completed") },
            cameraExecutor
        )

    previewView?.meteringPointFactory?.createPoint(.5f, .5f)
        ?.let { point ->
            FocusMeteringAction.Builder(point).disableAutoCancel().build()
        }
        ?.let { ac ->
            camera.cameraControl.startFocusAndMetering(ac)
        }
        ?.let {
            it.addListener({ Timber.d("Focus and metering action completed") }, cameraExecutor)
        }



    /*
                                    camera.cameraControl.setExposureCompensationIndex(6)
                                    camera.cameraControl.enableTorch(false)
    */

    /*
                                    camera.cameraInfo.exposureState.let { exposureState ->
                                        if (exposureState.isExposureCompensationSupported) {
                                            val range = exposureState.exposureCompensationRange
                                            val targetCompensation = (range.upper / 3)
                                            camera.cameraControl.setExposureCompensationIndex(targetCompensation)
                                        }
                                    }
    */

}