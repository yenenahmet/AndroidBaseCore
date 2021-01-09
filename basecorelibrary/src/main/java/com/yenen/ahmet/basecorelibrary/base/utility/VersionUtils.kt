package com.yenen.ahmet.basecorelibrary.base.utility

import android.app.Activity
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.pm.PackageInstaller
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.IOException


object VersionUtils {
 // https://gitlab.com/commonsguy/cw-android-q/-/blob/vFINAL/AppInstaller/src/main/java/com/commonsware/q/appinstaller/InstallReceiver.kt
    val NAME = "mostly-unused"
    val PI_INSTALL = 3439


    @Throws(IOException::class)
    fun installApk(file: File, app: Application, authority: String,broadcastReceiverClass: Class<Any>){
        val apkUri = FileProvider.getUriForFile(
            app,
            authority,
            file
        )
        installApk(app,apkUri,broadcastReceiverClass)
    }

    @Throws(IOException::class)
    fun installApk(application: Application, apkUri: Uri, broadcastReceiverClass: Class<Any>) {
        val resolver = application.contentResolver
        val installer = application.packageManager.packageInstaller

        resolver.openInputStream(apkUri)?.use { apkStream ->
            val length =
                DocumentFile.fromSingleUri(application, apkUri)?.length() ?: -1
            val params =
                PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            val sessionId = installer.createSession(params)
            val session = installer.openSession(sessionId)

            session.openWrite(NAME, 0, length).use { sessionStream ->
                apkStream.copyTo(sessionStream)
                session.fsync(sessionStream)
                val intent = Intent(application, broadcastReceiverClass)
                val pi = PendingIntent.getBroadcast(
                    application,
                    PI_INSTALL,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                session.commit(pi.intentSender)
            }
        }
    }


}