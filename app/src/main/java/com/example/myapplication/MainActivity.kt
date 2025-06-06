package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import com.example.myapplication.data.RentalManager
import com.example.myapplication.model.Customer
import com.example.myapplication.service.SpeedMonitorService

//UI to start/stop rental sessions.

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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