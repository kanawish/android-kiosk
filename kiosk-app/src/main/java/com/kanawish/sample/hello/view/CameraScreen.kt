package com.kanawish.sample.hello.view

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kanawish.sample.hello.navigation.MainNav
import com.kanawish.sample.hello.navigation.TypeRoute.QRScanRoute
import org.koin.compose.koinInject

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraScreen(
    mainNav: MainNav = koinInject()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button
        Button(
            onClick = { mainNav.navBack(QRScanRoute) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Back to Main")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // QR Scanner
        QRScannerScreen()
    }
} 