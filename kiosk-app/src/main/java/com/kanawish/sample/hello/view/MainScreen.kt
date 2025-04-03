package com.kanawish.sample.hello.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kanawish.sample.hello.di.previewModule
import com.kanawish.sample.hello.theme.AppTheme
import org.koin.compose.KoinApplication

@Preview(device = Devices.PIXEL_TABLET)
@Composable
fun MainScreenPreview() {
    KoinApplication(application = { modules(previewModule()) }) {
        AppTheme(darkTheme = false) {
            Surface {
                MainScreen(
                    kioskControls = { KioskControls(true) },
                    cameraDiagnostics = {
                        CameraDiagnostics(skipDiagnostics = true)
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    kioskControls: @Composable () -> Unit = { KioskControls() },
    cameraDiagnostics: @Composable () -> Unit = { CameraDiagnostics() },
) {
    var selectedItem by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Main content
        Row(modifier = Modifier
            .weight(1f)
            .padding(16.dp)
        ) {
            // Kiosk Controls
            Column {
                kioskControls()
                Text("Hello, World!")
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Camera Diagnostics
            cameraDiagnostics()
        }

        MainNavigationBar(
            selectedItem = selectedItem,
            onNavigationItemSelected = { selectedItem = it }
        )
    }
}

@Composable
private fun MainNavigationBar(
    selectedItem: Int,
    onNavigationItemSelected: (Int) -> Unit
) {
    val items = listOf("Home", "Settings")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Settings)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { onNavigationItemSelected(index) }
            )
        }
    }
}