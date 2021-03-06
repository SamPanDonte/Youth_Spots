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

    companion object {
        private const val CHANNEL_ID = "GeofenceNotification"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            Log.e(
                "Geofence Error: ",
                GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            )
            return
        }
        when (geofencingEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> enterGeofence(geofencingEvent.triggeringGeofences, context)
            Geofence.GEOFENCE_TRANSITION_EXIT -> exitGeofence(geofencingEvent.triggeringGeofences, context)
        }
    }

    private fun enterGeofence(geofence: List<Geofence>, context: Context) {
        removeGeofence(geofence, context)
        if (geofence.size == 1) {
            sendNotification(context, context.getString(R.string.entered_one))
        } else {
            sendNotification(context, context.getString(R.string.entered_many))
        }
    }

    private fun exitGeofence(geofence: List<Geofence>, context: Context) {
        removeGeofence(geofence, context)
        if (geofence.size == 1) {
            sendNotification(context, context.getString(R.string.exited_one))
        } else {
            sendNotification(context, context.getString(R.string.exited_many))
        }
    }

    private fun removeGeofence(geofenceList: List<Geofence>, ctx: Context) {
        LocationServices.getGeofencingClient(ctx).removeGeofences(geofenceList.map { it.requestId })
    }

    private fun createChannel(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                ctx.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = ctx.getString(R.string.channel_description) }
            getSystemService(ctx, NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(ctx: Context, text: String) {
        createChannel(ctx)
        val notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setContentTitle(ctx.getString(R.string.notification_title))
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_baseline_location_on_24)
            .setContentIntent(PendingIntent.getActivity(
                    ctx, 0, Intent(ctx, MainActivity::class.java), 0
            )).build()
        NotificationManagerCompat.from(ctx).notify(0, notification)
    }
}