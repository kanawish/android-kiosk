package com.kanawish.sample.hello.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kanawish.sample.hello.KioskContent

@Composable
fun MainScreen(
    isLockTaskMode: Boolean = false,
    onToggleLockTask: () -> Unit = {},
    onEndApp: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Kiosk Controls
        KioskContent(
            isLockTaskMode = isLockTaskMode,
            onToggleLockTask = onToggleLockTask,
            onEndApp = onEndApp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Camera Diagnostics
        CameraDiagnostics()

        Spacer(modifier = Modifier.height(16.dp))

    }
} 