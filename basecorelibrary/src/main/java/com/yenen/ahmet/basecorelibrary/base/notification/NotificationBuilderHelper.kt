package com.yenen.ahmet.basecorelibrary.base.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationBuilderHelper {

    fun getNotificationCompatBuilder(
        context: Context,
        cls: Class<*>,
        smlIcon: Int
    ): NotificationCompat.Builder {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val adminChannel =
                NotificationChannel("baseSS", "Name", NotificationManager.IMPORTANCE_HIGH)
            adminChannel.description = "Desc"
            adminChannel.enableLights(true)
            adminChannel.lightColor = Color.RED
            adminChannel.enableVibration(true)
            notificationManager.createNotificationChannel(adminChannel)
        }


        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, "baseSS")
            .setSmallIcon(smlIcon)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(getPendingIntent(context, cls))

        return notificationBuilder
    }

    fun getPendingIntent(context: Context, cls: Class<*>): PendingIntent {
        val notificationIntent = Intent(context, cls)
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        return PendingIntent.getActivity(context, 0, notificationIntent, 0)
    }

    fun notificationManagerNotify(builder: NotificationCompat.Builder, context: Context) {
        val notificationId = Random().nextInt(60000)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }
}