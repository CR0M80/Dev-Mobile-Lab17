package com.example.receiverdemo

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var airplaneReceiver: AirplaneModeReceiver
    private lateinit var customReceiver: CustomEventReceiver
    
    private var isAirplaneRegistered = false
    
    private lateinit var tvAirplaneStatus: TextView
    private lateinit var tvLogs: TextView
    private lateinit var btnToggleAirplane: MaterialButton
    private lateinit var btnSendCustom: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Initialize Views
        tvAirplaneStatus = findViewById(R.id.tvAirplaneStatus)
        tvLogs = findViewById(R.id.tvLogs)
        btnToggleAirplane = findViewById(R.id.btnToggleAirplane)
        btnSendCustom = findViewById(R.id.btnSendCustom)

        // Initialize Receivers with UI update logic
        airplaneReceiver = AirplaneModeReceiver { isOn ->
            updateAirplaneUI(isOn)
            addLog("Mode Avion: ${if (isOn) "ACTIVÉ" else "DÉSACTIVÉ"}")
        }

        customReceiver = CustomEventReceiver { message ->
            addLog("Custom Broadcast: $message")
        }

        btnToggleAirplane.setOnClickListener { toggleAirplaneReceiver() }
        btnSendCustom.setOnClickListener { sendCustomBroadcast() }
        
        // Register custom receiver for the duration of the activity
        registerReceiver(customReceiver, IntentFilter("com.example.receiverdemo.CUSTOM_EVENT"), RECEIVER_NOT_EXPORTED)
    }

    private fun toggleAirplaneReceiver() {
        if (!isAirplaneRegistered) {
            val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            registerReceiver(airplaneReceiver, filter)
            isAirplaneRegistered = true
            
            btnToggleAirplane.text = "Désactiver l'écoute"
            btnToggleAirplane.setStrokeColorResource(android.R.color.holo_red_light)
            tvAirplaneStatus.text = "Écoute active..."
            tvAirplaneStatus.setTextColor(Color.parseColor("#6C5CE7"))
            addLog("Système: Écoute Mode Avion démarrée")
        } else {
            unregisterReceiver(airplaneReceiver)
            isAirplaneRegistered = false
            
            btnToggleAirplane.text = "Activer l'écoute"
            btnToggleAirplane.setStrokeColor(null)
            tvAirplaneStatus.text = "Receiver non enregistré"
            tvAirplaneStatus.setTextColor(Color.parseColor("#636E72"))
            addLog("Système: Écoute Mode Avion arrêtée")
        }
    }

    private fun sendCustomBroadcast() {
        val intent = Intent("com.example.receiverdemo.CUSTOM_EVENT").apply {
            putExtra("message", "Vibration temporelle n°${(1..100).random()}")
            // Essential for Android 14+ when sending to own receivers
            setPackage(packageName) 
        }
        sendBroadcast(intent)
        addLog("Action: Émission d'un signal custom")
    }

    private fun updateAirplaneUI(isOn: Boolean) {
        tvAirplaneStatus.text = if (isOn) "STATUT: HORS LIGNE ✈️" else "STATUT: EN LIGNE 🌐"
        tvAirplaneStatus.setTextColor(if (isOn) Color.RED else Color.parseColor("#00B894"))
    }

    private fun addLog(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val currentLogs = tvLogs.text.toString()
        val newLog = "[$timestamp] $message\n$currentLogs"
        tvLogs.text = newLog
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isAirplaneRegistered) {
            unregisterReceiver(airplaneReceiver)
        }
        unregisterReceiver(customReceiver)
    }
}
