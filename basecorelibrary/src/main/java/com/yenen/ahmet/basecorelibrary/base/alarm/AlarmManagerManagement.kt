package com.yenen.ahmet.basecorelibrary.base.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import java.util.*
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build


class AlarmManagerManagement(private val context: Context) {

    fun setAlarmRepetitive(date: Date, requestCode: Int) {
        val calender = Calendar.getInstance()
        calender.timeInMillis = System.currentTimeMillis()
        calender.time = date

        val receiver = ComponentName(context, BaseAlarmReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        val manager =  context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nt = Intent(context,BaseAlarmReceiver::class.java)
        nt.putExtra("REQUEST_CODE",requestCode)
        val alarmIntent = PendingIntent.getBroadcast(context,requestCode,nt,0)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,  calender.getTimeInMillis(), alarmIntent);
        }else{
            manager.setExact(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), alarmIntent);
        }
    }


    fun cancelAlarm(requestCode:Int){
        val receiver = ComponentName(context, BaseAlarmReceiver::class.java)
        val pm = context.getPackageManager()
        pm.setComponentEnabledSetting(receiver,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP)

        val intent = Intent(context, BaseAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}