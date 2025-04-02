// BootReceiver.kt
package com.kanawish.sample.hello

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Timber.d("Boot completed, doing nothing for now.")
/*
            // NOTE: This is not listed in the manifest for now, purposefully-ish
            val startIntent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(startIntent)
*/
        }
    }
}