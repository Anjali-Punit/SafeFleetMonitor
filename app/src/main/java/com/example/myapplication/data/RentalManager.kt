package com.example.myapplication.data

import com.example.myapplication.model.Customer
import com.example.myapplication.service.AwsSnsNotifier

// This file manages which customer is currently renting a car.

object RentalManager {
    private var currentCustomer: Customer? = null
    private val snsNotifier = AwsSnsNotifier()


    fun startRental(customer: Customer) { //sets the current customer
        currentCustomer = customer

        // OPTIONAL: Notify AWS SNS
        val message = "Rental started for customer: ${customer.name}, Max Speed: ${customer.maxSpeed} km/h"
        snsNotifier.sendEmail(message)
        snsNotifier.sendSms(message, "+911234567890") // Use actual fleet manager number
    }

    fun getCurrentCustomer(): Customer? = currentCustomer // returns customer info

    fun stopRental() {

        currentCustomer?.let {
            // OPTIONAL: Notify AWS SNS
            val message = "Rental ended for customer: ${it.name}"
            snsNotifier.sendEmail(message)
            snsNotifier.sendSms(message, "+911234567890")
        }

        currentCustomer = null // ends session and resets
    }
}