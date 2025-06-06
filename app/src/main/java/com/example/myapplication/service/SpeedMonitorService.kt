package com.example.myapplication.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import com.example.myapplication.data.RentalManager
import com.example.myapplication.util.SpeedUtils
import kotlin.random.Random

//Background service that checks speed and triggers alerts.

class SpeedMonitorService : Service() {

    private val handler = Handler()
    private val checkInterval = 3000L // Check every 3 seconds , Simulates speed every 3 seconds

    private val speedCheckRunnable = object : Runnable {
        override fun run() {
            val currentSpeed = simulateCurrentSpeed()
            val customer = RentalManager.getCurrentCustomer() //  Compares with customer’s limit
            customer?.let {
                if (SpeedUtils.isOverSpeedLimit(currentSpeed, it.maxSpeed)) {
                    showWarning(currentSpeed) //If exceeded:  Shows a Toast warning

                    FirebaseNotifier.notifyFleet(it.id, currentSpeed) //Notifies Firebase
                }
            }
            handler.postDelayed(this, checkInterval)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(speedCheckRunnable)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(speedCheckRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun simulateCurrentSpeed(): Int =
        Random.nextInt(50, 140) // Runs repeatedly until stopped

    private fun showWarning(speed: Int) {
        Toast.makeText(this, "Warning! Speed exceeded: $speed km/h", Toast.LENGTH_SHORT).show()
    }
}