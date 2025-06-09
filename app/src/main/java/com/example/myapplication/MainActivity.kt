package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.data.RentalManager
import com.example.myapplication.model.Customer
import com.example.myapplication.service.SpeedMonitorService

//UI to start/stop rental sessions.

class MainActivity : AppCompatActivity() {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("MissingInflatedId", "ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request permission for location
        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        val startButton: Button = findViewById(R.id.startRentalButton)
        val stopButton: Button = findViewById(R.id.stopRentalButton)

        startButton.setOnClickListener {
            val customer = Customer("cust_001", "John Doe", 100)
            RentalManager.startRental(customer)
            startService(Intent(this, SpeedMonitorService::class.java))
        }

        stopButton.setOnClickListener {
            RentalManager.stopRental()
            stopService(Intent(this, SpeedMonitorService::class.java))
        }
    }

}