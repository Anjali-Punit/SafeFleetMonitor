package com.example.myapplication.data

import com.example.myapplication.model.Customer

// This file manages which customer is currently renting a car.

object RentalManager {
    private var currentCustomer: Customer? = null

    fun startRental(customer: Customer) { //sets the current customer
        currentCustomer = customer
    }

    fun getCurrentCustomer(): Customer? = currentCustomer // returns customer info

    fun stopRental() {
        currentCustomer = null // ends session and resets
    }
}