package com.example.myapplication.util

//Utility function to check if speed is over limit.

object SpeedUtils {
    fun isOverSpeedLimit(currentSpeed: Int, maxSpeed: Int): Boolean {
        return currentSpeed > maxSpeed
    }
}