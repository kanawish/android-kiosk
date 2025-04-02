package com.kanawish.sample.hello

import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber
import androidx.navigation.compose.rememberNavController
import com.kanawish.sample.hello.navigation.MainNav
import com.kanawish.sample.hello.navigation.MainNavGraph
import com.kanawish.sample.hello.navigation.TypeRoute.QRScanRoute
import org.koin.compose.koinInject

/**
 * PlantUML diagram for the Kiosk App structure:
 * 
 * ```
 * @startuml
 * 
 * class KioskActivity {
 *   - devicePolicyManager: DevicePolicyManager
 *   + onCreate(savedInstanceState: Bundle?)
 *   - isLockTaskPermitted(): Boolean
 * }
 * 
 * ComponentActivity <|-- KioskActivity
 * 
 * class "ImmersiveModeEffect()" as ImmersiveModeEffect {
 *   DisposableEffect that hides system UI
 * }
 * 
 * class "KioskLockTaskEffect()" as KioskLockTaskEffect {
 *   DisposableEffect that handles lock task mode
 * }
 * 
 * class "KioskContent()" as KioskContent {
 *   The main UI content displayed in kiosk mode
 * }
 * 
 * KioskActivity --> ImmersiveModeEffect : uses
 * KioskActivity --> KioskLockTaskEffect : uses
 * KioskActivity --> KioskContent : displays
 * 
 * @enduml
 * ```
 */
class KioskActivity : ComponentActivity() {
    private lateinit var devicePolicyManager: DevicePolicyManager

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        // Get device policy manager
        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        
        // Set up Compose UI
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Apply immersive mode effect to hide system UI
                    ImmersiveModeEffect()

                    // Kiosk lock task effect - handles starting/stopping lock task mode
                    KioskLockTaskEffect()

                    // Main content with callbacks
                    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    val isLockTaskMode = remember {
                        mutableStateOf(
                            try {
                                activityManager.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE
                            } catch (e: Exception) {
                                Timber.w(e, "Failed to check lock task mode state")
                                false
                            }
                        )
                    }

                    // Navigation graph
                    MainNavGraph(
                        isLockTaskMode = isLockTaskMode.value,
                        onToggleLockTask = {
                            try {
                                if (isLockTaskMode.value) {
                                    stopLockTask()
                                    Timber.d("Lock task stopped manually")
                                } else {
                                    startLockTask()
                                    Timber.d("Lock task started manually")
                                }
                                // Update status after toggling lock task
                                isLockTaskMode.value = activityManager.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE
                            } catch (e: Exception) {
                                Timber.w(e, "Failed to toggle lock task mode")
                            }
                        },
                        onEndApp = {
                            try {
                                if (isLockTaskMode.value) {
                                    stopLockTask()
                                }
                                finishAndRemoveTask()
                                Timber.d("Activity finished and removed from recent tasks")
                            } catch (e: Exception) {
                                Timber.w(e, "Failed to finish activity")
                            }
                        }
                    )
                }
            }
        }
    }
    
    /**
     * Checks if this app is allowed to use lock task mode
     */
    private fun isLockTaskPermitted(): Boolean {
        return try {
            devicePolicyManager.isLockTaskPermitted(packageName)
        } catch (e: SecurityException) {
            Timber.w(e, "App is not a device owner, lock task permission check failed")
            false
        }
    }
}

/**
 * Composable effect that applies and maintains immersive mode
 */
@Composable
fun ImmersiveModeEffect() {
    val context = LocalContext.current
    DisposableEffect(true) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose { }
        
        val window = activity.window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        
        // Hide both the status bar and the navigation bar
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        
        // Set up lifecycle observer to maintain immersive mode
        val lifecycleObserver = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
        }
        
        val lifecycle = (context as ComponentActivity).lifecycle
        lifecycle.addObserver(lifecycleObserver)
        
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}

/**
 * Composable effect that handles lock task mode
 */
@Composable
fun KioskLockTaskEffect() {
    val context = LocalContext.current
    
    DisposableEffect(true) {
        val activity = context as? ComponentActivity ?: return@DisposableEffect onDispose { }
        
        // Start lock task on resume, if possible
        val lifecycleObserver = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                try {
                    // Note: This will only work if:
                    // 1. The app is a device owner app, OR
                    // 2. The app has been allowlisted by a device owner
                    activity.startLockTask()
                    Timber.d("Lock task started")
                } catch (e: Exception) {
                    Timber.w(e, "Failed to start lock task - app likely not authorized")
                }
            }
        }
        
        val lifecycle = activity.lifecycle
        lifecycle.addObserver(lifecycleObserver)
        
        onDispose {
            try {
                activity.stopLockTask()
                Timber.d("Lock task stopped")
            } catch (e: Exception) {
                Timber.w(e, "Failed to stop lock task")
            }
            
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}

/**
 * Main kiosk content
 */
@Composable
fun KioskContent(
    mainNav: MainNav = koinInject(),
    isLockTaskMode: Boolean,
    onToggleLockTask: () -> Unit,
    onEndApp: () -> Unit,
) {
    Column(
        modifier = Modifier
//            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        
        Text(
            text = "ODROID Kiosk Mode",
            fontSize = 32.sp,
            color = Color.Blue,
            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(30.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isLockTaskMode) Color.Green else Color.Red,
                modifier = Modifier.size(12.dp)
            ) { }
            
            Text(
                text = if (isLockTaskMode) " Lock Task Mode: Active 🔒" else " Lock Task Mode: Inactive 🔓",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onToggleLockTask,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(if (isLockTaskMode) "Exit Kiosk Mode" else "Enter Kiosk Mode")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Button
            Button(
                onClick = { mainNav.nav(QRScanRoute) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Open Camera View")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onEndApp,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("End App ☠️")
            }
        }
    }
}