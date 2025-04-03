package com.kanawish.sample.hello

import android.app.admin.DevicePolicyManager
import android.os.Bundle
import android.os.Process.killProcess
import android.os.Process.myPid
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kanawish.sample.hello.model.ActivityTaskEvent.END_APP
import com.kanawish.sample.hello.model.ActivityTaskEvent.LOCK
import com.kanawish.sample.hello.model.ActivityTaskEvent.UNLOCK
import com.kanawish.sample.hello.model.KioskModel
import com.kanawish.sample.hello.navigation.MainNavGraph
import com.kanawish.sample.hello.theme.AppTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI
import timber.log.Timber

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
@OptIn(KoinExperimentalAPI::class)
class KioskActivity : ComponentActivity() {
    private val devicePolicyManager: DevicePolicyManager by inject()
    private val kioskModel: KioskModel by inject()

    fun launchKioskEventHandling() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                kioskModel.activityTaskEvent.collect { event ->
                    Timber.d("Received activity task event: $event")
                    val lockTaskPermitted = isLockTaskPermitted()
                    try {
                        when (event) {
                            LOCK -> {
                                Timber.d("Attempt to start lock task mode")
                                if (lockTaskPermitted) startLockTask()
                            }
                            UNLOCK -> {
                                Timber.d("Attempt to stop lock task mode")
                                if (lockTaskPermitted) stopLockTask()
                            }
                            END_APP -> {
                                Timber.d("andApp called: finish/remove task, force stop our own process.")
                                try {
                                    // Testing that process won't respawn with this... looks like it.
                                    stopLockTask()
                                    // Finish and remove from recent tasks
                                    finishAndRemoveTask()
                                    // Force stop the process
                                    killProcess(myPid())
                                } catch (e: Exception) {
                                    Timber.w(e, "Failed to finish activity")
                                }
                            }
                        }
                        if (!lockTaskPermitted) Timber.w("NOTE: Lock task was not permitted")
                    } catch (e: Exception) {
                        Timber.w(e, "Failed to toggle lock task mode.")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        launchKioskEventHandling()

        lifecycleScope.launch {
            Timber.d("🔍 Inside launch, about to collect... $kioskModel")
            kioskModel.activityTaskEvent.collectLatest { event ->
                Timber.d("🤔 Received activity task event: $event")
            }
            Timber.d("🔍 Collection ended") // This line will only run if collection is cancelled
        }

        // Set up Compose UI
        setContent {
            AppTheme {
                KoinAndroidContext {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Apply immersive mode effect to hide system UI
                        // ImmersiveModeEffect()

                        // Navigation graph
                        MainNavGraph()
                    }
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

