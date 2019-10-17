package com.yenen.ahmet.basecorelibrary.base.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/* Manifrest ADDED
  <receiver
            android:name="yourpackagename.AlarmReceiver"
            android:exported="true"
            android:enabled="false">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
 */
abstract class BaseAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            if ("android.intent.action.BOOT_COMPLETED".equals(intent?.action)) {
                reStartAlarm(context)
                // alarms will be re-set
                return
            }
            if (intent != null)
                showAlarm(it, intent)
        }
    }

    protected abstract fun reStartAlarm(context: Context)

    protected abstract fun showAlarm(context: Context, intent: Intent)
}