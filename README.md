# SpeedSense

**Smart Speed Monitoring System for Car Rentals**

SpeedSense is a Kotlin-based Android Automotive OS (AAOS) application designed for car rental
companies to monitor renter driving behavior in real-time. If a vehicle exceeds the permitted speed
limit (defined per customer), the system alerts the renter and notifies the fleet company using
Firebase, with optional AWS SNS integration.

---

## Use Case

A car rental company wants to be alerted if a renter drives at a speed above a predefined limit.
Each customer can have their own speed limit, set before the rental begins. If the speed is
exceeded:

- ✅ The **renter receives an in-car warning**
- ✅ The **rental company is notified** via Firebase Cloud Messaging (FCM)
- 🟡 Optionally, **AWS SNS** can be used to send email or SMS alerts

---

## 🚀 Features

- Real-time vehicle speed tracking
- Per-customer configurable speed limits
- In-car warnings when limits are exceeded
- Notifications to the rental company via Firebase
- Optional integration with AWS SNS (SMS/email)
- Designed for Android Automotive OS

---

## Setup & Configuration

### 1. Clone the Repository

```bash
git clone https://github.com/Anjali-Punit/SafeFleetMonitor.git
cd SpeedSense

 ### 2. Open in Android Studio
- Requires Android Studio Hedgehog or newer

-AAOS emulator or real device recommended


 ### 3. Firebase Setup

 - Go to Firebase Console

 - Create a new project

 - Add your app’s package name (e.g., com.fleetguardian.app)

 - Download google-services.json

 -Place it in the app/ directory

 - Ensure you have Firebase Cloud Messaging enabled in your Firebase project.

 ### 4. AWS SNS (Optional)
 - To enable AWS SNS integration:

 - Create an IAM user with SNS permissions

 - Save your credentials in a secure way (use environment variables or encrypted config)

 - Configure AwsSnsNotifier with your Topic ARN and region

   ### 5. Permissions Required  
  
  ### 6. To function properly, the app needs:-
 - <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 - <uses-permission android:name="android.permission.INTERNET" />
 - <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

  ### 7. Testing the App
 - Start a rental session with a customer-specific speed limit

 - Inject mock or real GPS speed data (e.g., 80 km/h)

 - If the speed exceeds the limit:

 - Renter is alerted via Toast or Notification

 - Firebase or AWS SNS sends a message to the company

 -In real implementation, you'd pull speed data via GPS or AAOS Vehicle APIs.

### 8. Updated Plan for Real Device Testing (AAOS):
We'll modify SpeedMonitorService to:

Check if Car and CarPropertyManager are available.

If yes, use PERF_VEHICLE_SPEED.

Else, fallback to GPS-based speed (optional fallback).


