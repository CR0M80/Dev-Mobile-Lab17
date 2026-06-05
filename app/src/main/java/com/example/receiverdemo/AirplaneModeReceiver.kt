package com.example.receiverdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeReceiver(private val onStatusChanged: (Boolean) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED == intent.action) {
            val isAirplaneOn = intent.getBooleanExtra("state", false)
            
            // Notify UI via callback
            onStatusChanged(isAirplaneOn)
            
            val message = if (isAirplaneOn) 
                "🚀 Mode Avion ACTIVÉ - Silence radio !" 
            else 
                "🌍 Mode Avion DÉSACTIVÉ - Back online !"
            
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
