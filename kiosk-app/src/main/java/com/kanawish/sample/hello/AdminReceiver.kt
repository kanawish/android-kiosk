// AdminReceiver.kt
package com.kanawish.sample.hello

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * DeviceAdminReceiver implementation for kiosk mode.
 * This class handles device admin activation and deactivation events.
 */
class AdminReceiver : DeviceAdminReceiver() {
    companion object {
        private const val TAG = "AdminReceiver"
        
        /**
         * Gets a ComponentName for this receiver, to be used in device policy manager operations.
         * @param context The application context
         * @return ComponentName for this receiver
         */
        fun getComponentName(context: Context): ComponentName {
            return ComponentName(context.applicationContext, AdminReceiver::class.java)
        }
    }

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Log.i(TAG, "Device admin enabled")
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Log.i(TAG, "Device admin disabled")
    }

    override fun onLockTaskModeEntering(context: Context, intent: Intent, pkg: String) {
        super.onLockTaskModeEntering(context, intent, pkg)
        Log.i(TAG, "Lock task mode enabled")
    }

    override fun onLockTaskModeExiting(context: Context, intent: Intent) {
        super.onLockTaskModeExiting(context, intent)
        Log.i(TAG, "Lock task mode disabled")
    }
}