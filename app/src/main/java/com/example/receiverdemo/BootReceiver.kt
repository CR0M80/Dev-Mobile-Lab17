package com.example.receiverdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val logMessage = "⚡ Système initialisé : BootReceiver a détecté le démarrage !"
            Log.d("BootReceiver", logMessage)
            Toast.makeText(context, logMessage, Toast.LENGTH_LONG).show()
        }
    }
}
