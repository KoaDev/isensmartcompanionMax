package fr.isen.champion.isensmartcompanion.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log
import fr.isen.champion.isensmartcompanion.R
import fr.isen.champion.isensmartcompanion.helper.NotificationHelper

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "NotificationReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive called with intent: ${intent.extras}")

        val title = intent.getStringExtra("title") ?: "Event Reminder"
        val desc = intent.getStringExtra("desc") ?: "No details available"

        // Create the notification channel if needed
        NotificationHelper.createNotificationChannelIfNeeded(context)

        // Build the notification
        val builder = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure this drawable exists
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Check if we have POST_NOTIFICATIONS permission (Android 13+).
        // If not granted, we can't post the notification.
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Missing POST_NOTIFICATIONS permission; skipping notification.")
            return
        }

        // If we have permission, show the notification
        NotificationManagerCompat.from(context).notify(title.hashCode(), builder.build())
        Log.d(TAG, "Notification posted for title: $title")
    }
}
