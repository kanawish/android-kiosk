package com.kanawish.sample.hello.view

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@ExperimentalGetImage
@Composable
fun QRScannerScreen() {
    val context = LocalContext.current
    var showPermissionRequest:Boolean by remember { mutableStateOf(true) }
    
    // Request camera permission
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        showPermissionRequest = !isGranted
    }

    LaunchedEffect(Unit) {
        launcher.launch(android.Manifest.permission.CAMERA)
    }

    if (showPermissionRequest) {
        // Show permission request UI
        Text("Camera permission is required")
    } else {
        QRScanner { qrContent ->
            // Handle scanned QR code content
            // FIXME: Toast? Ugh.
            Toast.makeText(context, "Scanned: $qrContent", Toast.LENGTH_SHORT).show()
        }
    }
}
