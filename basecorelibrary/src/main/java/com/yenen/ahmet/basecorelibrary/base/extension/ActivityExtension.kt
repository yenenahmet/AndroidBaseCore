package com.yenen.ahmet.basecorelibrary.base.extension

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun AppCompatActivity.openFile(uri: Uri, fileType: String, title: String): Boolean {
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


fun AppCompatActivity.shareFile(
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



fun AppCompatActivity.openAppPermissionPage(): Boolean {
    return try {
        val intent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
        true
    } catch (ex: Exception) {
        false
    }
}




 fun AppCompatActivity.getMediaPath(uri: Uri?, mProjection: String): String? {
    uri?.let {
        val projection = arrayOf(mProjection)
        var path: String? = null
        contentResolver.query(it, projection, null, null, null)?.use {cursor->
            val idColumn  = cursor.getColumnIndexOrThrow(mProjection)
            cursor.moveToFirst()
            val contentUri: Uri = Uri.withAppendedPath(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                cursor.getString(idColumn)
            )
            path = contentUri.toString()
        }
        return path
    }
    return null
}

fun AppCompatActivity.openForResultMediaImage(title: String, requestCode: Int) {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(Intent.createChooser(intent, title), requestCode)
}

 fun AppCompatActivity.openForResultMediaVideo(title: String, requestCode: Int) {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
    intent.type = "video/*"
    startActivityForResult(Intent.createChooser(intent, title), requestCode)
}


@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
fun AppCompatActivity.createFileForFileProvider():File{
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}

@Throws(IOException::class)
fun AppCompatActivity.takePicture(requestCode:Int,authority:String) :String{
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val file: File = createFileForFileProvider()

    val uri: Uri = FileProvider.getUriForFile(
        this,
        authority,
        file
    )
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    startActivityForResult(intent, requestCode)
    return file.absolutePath
}

fun AppCompatActivity.writeFileContent(uri: Uri): String {
    var filePath = ""
    contentResolver?.openInputStream(uri)?.use { selectedFileInputStream ->
        val certCacheDir =
            File(getExternalFilesDir(null),"Cache")
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

fun AppCompatActivity.getFileDisplayName(uri: Uri): String {
    var displayName = ""
    contentResolver
        .query(uri, null, null, null, null, null).use {
            if (it != null && it.moveToFirst()) {
                val index: Int = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                displayName = it.getString(index)
            }
        }
    return displayName
}

fun AppCompatActivity.openDocument(requestCode:Int) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        type = "*/*"
    }
    startActivityForResult(intent, requestCode)
}

fun AppCompatActivity.getFileSize(uri: Uri): String {
    var size = ""
    contentResolver?.query(uri, null, null, null, null, null).use {
        if (it != null && it.moveToFirst()) {
            val index: Int = it.getColumnIndex(OpenableColumns.SIZE)
            size = it.getString(index)
        }
    }
    return size
}

fun AppCompatActivity.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}


fun AppCompatActivity.hideKeyboard() {
    currentFocus?.let {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun AppCompatActivity.screenBarClear() {
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN)
}