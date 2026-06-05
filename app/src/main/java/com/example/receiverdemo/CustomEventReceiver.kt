package com.example.receiverdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CustomEventReceiver(private val onReceived: (String) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if ("com.example.receiverdemo.CUSTOM_EVENT" == intent.action) {
            val message = intent.getStringExtra("message") ?: "Pas de message"
            onReceived(message)
            Toast.makeText(context, "📡 Signal reçu : $message", Toast.LENGTH_SHORT).show()
        }
    }
}
