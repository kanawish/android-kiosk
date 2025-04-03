package com.kanawish.sample.hello.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanawish.sample.hello.di.previewModule
import com.kanawish.sample.hello.navigation.MainNav
import com.kanawish.sample.hello.navigation.TypeRoute
import com.kanawish.sample.hello.model.KioskModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
@Preview(showBackground = true)
fun KioskContentPreview() {
    KoinApplication(application = { modules(previewModule()) }) {
        MaterialTheme { KioskControls(true) }
    }
}

/**
 * Main kiosk content
 */
@Composable
fun KioskControls(
    kioskModel: KioskModel = koinInject(),
    mainNav: MainNav = koinInject(),
) {
    val appState by kioskModel.state.collectAsState()
    val isLockTaskMode = !appState.unlockedTaskMode

    KioskControls(
        isLockTaskMode,
        { kioskModel.toggleLockTaskMode() },
        { kioskModel.endApp() },
        { mainNav.nav(TypeRoute.QRScanRoute) }
    )
}

@Composable
fun KioskControls(
    isLockTaskMode: Boolean,
    onToggleLockTask: () -> Unit = {},
    onEndApp: () -> Unit = {},
    onNavigateToQRScan: () -> Unit = {}
) {

    Column(
        modifier = Modifier.Companion.padding(16.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.Companion.height(30.dp))

        Text(
            text = "ODROID Kiosk Mode",
            fontSize = 32.sp,
            color = Color.Companion.Blue,
            textAlign = TextAlign.Companion.Center,
        )

        Spacer(modifier = Modifier.Companion.height(30.dp))

        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            modifier = Modifier.Companion.padding(8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isLockTaskMode) Color.Companion.Green else Color.Companion.Red,
                modifier = Modifier.Companion.size(12.dp)
            ) { }

            Text(
                text = if (isLockTaskMode) " Lock Task Mode: Active 🔒" else " Lock Task Mode: Inactive 🔓",
                modifier = Modifier.Companion.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.Companion.height(4.dp))

        Row(verticalAlignment = Alignment.Companion.CenterVertically) {
            Button(
                onClick = onToggleLockTask,
                modifier = Modifier.Companion.padding(16.dp)
            ) {
                Text(if (isLockTaskMode) "Exit Kiosk Mode" else "Enter Kiosk Mode")
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            // Navigation Button
            Button(
                onClick = onNavigateToQRScan,
                modifier = Modifier.Companion.padding(16.dp)
            ) {
                Text("QR Scanner")
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            Button(
                onClick = onEndApp,
                modifier = Modifier.Companion.padding(16.dp)
            ) {
                Text("End App ☠️")
            }
        }
    }
}
