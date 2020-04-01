package com.yenen.ahmet.basecorelibrary.base.utility

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File

object VersionUtils {


    fun openFileApk(filePath:String,context:Activity, authority:String){
        val file = File(filePath)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
            val apkUri = FileProvider.getUriForFile(
                context,
                authority,
                file
            )
            intent.data = apkUri
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            val apkUri = Uri.fromFile(file)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun openUnknownSource(context: Activity,requestCode:Int,packageName:String):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !context.packageManager.canRequestPackageInstalls()) {
            val nt = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            nt.data = Uri.parse(String.format("package:%s",packageName))
            context.startActivityForResult(nt, requestCode)
            return false
        }else{
            return true
        }
    }


}