package com.example.youthspots.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.youthspots.R
import com.example.youthspots.ui.activity.MainActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.LocationServices

class GeofenceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e("TAG", errorMessage)
            return
        }
        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            val geofencingClient = LocationServices.getGeofencingClient(context)
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            geofencingClient.removeGeofences(triggeringGeofences.map { it.requestId })

            createChannel(context)
            if (triggeringGeofences.size == 1) {
                sendNotification(context, "You entered chosen point of interest!")
            } else {
                sendNotification(context, "You entered chosen points of interest!")
            }
        }
    }

    fun sendNotification(context: Context, text: String) {
        val builder = NotificationCompat.Builder(context, "GeofenceNotification")
            .setContentTitle("Entered point of interest!")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    0
                )
            )
        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "GeofenceNotification"
            val descriptionText = "GeofenceNotification" // TODO
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("GeofenceNotification", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(context, NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)
        }
    }
}