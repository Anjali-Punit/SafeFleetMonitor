package com.example.myapplication.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority

import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import com.example.myapplication.data.RentalManager
import com.example.myapplication.model.Customer
import com.example.myapplication.util.SpeedUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlin.math.roundToInt

//Background service that checks speed and triggers alerts.

class SpeedMonitorService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var snsNotifier: AwsSnsNotifier

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        snsNotifier = AwsSnsNotifier()
        startForegroundNotification()
        startSpeedTracking()
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun startSpeedTracking() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            3000L // Check every 3 seconds
        )
            .setMinUpdateIntervalMillis(2000L)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location: Location = result.lastLocation ?: return
                val speedKmph = (location.speed * 3.6).roundToInt() // Convert m/s to km/h

                val customer: Customer? = RentalManager.getCurrentCustomer()
                customer?.let {
                    if (SpeedUtils.isOverSpeedLimit(speedKmph, it.maxSpeed)) {
                        showWarning(speedKmph)

                        // Notify via Firebase
                        FirebaseNotifier.notifyFleet(it.id, speedKmph)

                        // Notify via AWS SNS
                        val alertMessage =
                            "Speed Violation Detected: $speedKmph km/h (limit: ${it.maxSpeed})"
                        snsNotifier.sendSms(alertMessage, "+911234567890") // Replace with real number
                        snsNotifier.sendEmail(alertMessage)
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun showWarning(speed: Int) {
        Toast.makeText(
            this,
            "⚠️ Speed exceeded: $speed km/h",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun startForegroundNotification() {
        val channelId = "speed_monitor_channel"
        val channelName = "Speed Monitor Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Speed Monitoring Active")
            .setContentText("Tracking GPS speed...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }
}