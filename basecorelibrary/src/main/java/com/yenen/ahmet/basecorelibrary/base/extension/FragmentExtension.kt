package com.yenen.ahmet.basecorelibrary.base.extension

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.yenen.ahmet.basecorelibrary.base.utility.FileUtils
import com.yenen.ahmet.basecorelibrary.base.utility.FileUtils.getUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.openFile(uri: Uri, fileType: String, title: String): Boolean {
    val target = Intent(Intent.ACTION_VIEW)
    target.setDataAndType(uri, fileType)
    target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    val intent = Intent.createChooser(target, title)
    return try {
        startActivity(intent)
        true
    } catch (e: ActivityNotFoundException) {
        false
    }
}


fun Fragment.shareFile(
    fileType: String,
    file: File,
    filUri: Uri,
    subject: String,
    chooserTitle: String
): Boolean {

    if (!file.exists()) {
        return false
    }

    val intentShareFile = Intent().apply {
        type = fileType
        putExtra(Intent.EXTRA_STREAM, filUri)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    Intent.createChooser(intentShareFile, chooserTitle)?.let {
        startActivity(it)
        return true
    }
    return false

}


fun Fragment.openAppPermissionPage(): Boolean {
    return try {
        val intent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", activity?.packageName, null)
        }
        activity?.startActivity(intent)
        true
    } catch (ex: Exception) {
        false
    }
}

@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
fun Fragment.createFileForFileProvider(): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}

@Throws(IOException::class)
fun Fragment.takePicture(requestCode: Int, authority: String): String {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val file: File = createFileForFileProvider()

    val uri: Uri = FileProvider.getUriForFile(
        activity!!,
        authority,
        file
    )
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    startActivityForResult(intent, requestCode)
    return file.absolutePath
}

fun Fragment.writeFileContent(uri: Uri): String {
    var filePath = ""
    activity?.contentResolver?.openInputStream(uri)?.use { selectedFileInputStream ->
        val certCacheDir =
            File(activity?.getExternalFilesDir(null), "Cache")
        var isCertCacheDirExists = certCacheDir.exists()
        if (!isCertCacheDirExists) {
            isCertCacheDirExists = certCacheDir.mkdirs()
        }

        if (isCertCacheDirExists) {
            filePath = "${certCacheDir.absolutePath}/${getFileDisplayName(uri)}"
            FileOutputStream(filePath).use { selectedFileOutPutStream ->
                val buffer = ByteArray(1024)
                while (true) {
                    val length = selectedFileInputStream.read(buffer)
                    if (length <= 0) {
                        break
                    }
                    selectedFileOutPutStream.write(buffer, 0, length)
                }
            }
        }
    }
    return filePath
}

fun Fragment.getFileDisplayName(uri: Uri): String {
    var displayName = ""
    activity?.contentResolver?.query(uri, null, null, null, null, null).use {
        if (it != null && it.moveToFirst()) {
            val index: Int = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            displayName = it.getString(index)
        }
    }
    return displayName
}


fun Fragment.openRingToneScreen(ringToneType: Int, uriPath: String, requestCode: Int,title:String):Boolean {
    getUri(uriPath)?.let { currentTone ->
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, ringToneType)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, title)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, currentTone)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        startActivityForResult(intent, requestCode)
        return true
    }
    return false
}

fun Fragment.showToast(text: String) {
    Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
}

fun Fragment.getFileSize(uri: Uri): String {
    var size = ""
    activity?.contentResolver?.query(uri, null, null, null, null, null).use {
        if (it != null && it.moveToFirst()) {
            val index: Int = it.getColumnIndex(OpenableColumns.SIZE)
            size = it.getString(index)
        }
    }
    return size
}

fun Fragment.openDocument(requestCode: Int) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        type = "*/*"
    }
    startActivityForResult(intent, requestCode)
}

fun Fragment.openUserFacebookMessenger(id: Long, warningMessage: String) {
    if (isPackageExisted("com.facebook.orca")) {
        var uri = Uri.parse("fb-messenger://user/")
        uri = ContentUris.withAppendedId(uri, id)
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.facebook.orca")
        }
        startActivity(intent)
    } else {
        showToast(warningMessage)
        val uri = Uri.parse("market://details?id=com.facebook.orca")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent);
    }
}

fun Fragment.shareTwitter(warningMessage: String, message: String) {
    if (isPackageExisted("com.twitter.android")) {
        val tweetIntent = Intent(Intent.ACTION_SEND).apply {
            setPackage("com.twitter.android")
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        startActivity(tweetIntent)
    } else {
        showToast(warningMessage)
        val uri = Uri.parse("market://details?id=com.twitter.android")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}

fun Fragment.isPackageExisted(targetPackage: String): Boolean {
    try {
        val pm = activity?.packageManager
        pm?.getPackageInfo(targetPackage, PackageManager.GET_META_DATA)
    } catch (ex: Exception) {
        return false
    }
    return true
}

fun Fragment.openFileDefaultAvailableApp(filePath: String): Boolean {
    val file = File(filePath)
    var mime = FileUtils.getMimeType(file.name)

    if (mime.isEmpty()) {
        mime = "*/*"
    }

    val uri = Uri.fromFile(file)
    return openFileDefaultAvailableApp(uri, mime)
}

fun Fragment.openFileDefaultAvailableApp(uri: Uri, mimeType: String): Boolean {
    val nt = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mimeType)
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    return try {
        startActivity(nt)
        true
    } catch (ex: Exception) {
        false
    }
}

fun Fragment.shareBip(warningMessage: String, mUri: Uri) {
    if (isPackageExisted("com.turkcell.bip")) {
        val intent = Intent(Intent.ACTION_VIEW, mUri)
        startActivity(intent)
    } else {
        showToast(warningMessage)
        val uri = Uri.parse("market://details?id=com.turkcell.bip")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}

fun Fragment.openNavigationGoogleMap(location:String):Boolean {
    val gmmIntentUri = Uri.parse("google.navigation:q=$location")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (mapIntent.resolveActivity(activity!!.packageManager) != null) {
        startActivity(mapIntent)
        return true
    }
    return false
}

fun Fragment.openNavigationYandexMap(latitude:String, longitude:String):Boolean {
    val yandex = Intent("ru.yandex.yandexnavi.action.BUILD_ROUTE_ON_MAP").apply {
        setPackage("ru.yandex.yandexnavi")
        putExtra("lat_to", latitude)
        putExtra("lon_to", longitude)
    }

    if (yandex.resolveActivity(activity!!.packageManager) != null) {
        startActivity(yandex)
        return true
    }
    return false
}