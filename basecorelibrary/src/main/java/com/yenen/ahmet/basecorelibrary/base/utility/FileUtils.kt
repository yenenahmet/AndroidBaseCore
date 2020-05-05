package com.yenen.ahmet.basecorelibrary.base.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {

    var createFilePath = ""

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createPicturesFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val file = File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
        createFilePath = file.absolutePath
        return file
    }


    fun takePicture(context: Activity, authority: String, requestCode: Int): Boolean {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File
        try {
            file = createPicturesFile(context)
        } catch (ex: Exception) {
            return false
        }

        val uri: Uri = FileProvider.getUriForFile(
            context,
            authority,
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        context.startActivityForResult(intent, requestCode)
        return true
    }


    fun saveBitmapToFile(bitmap: Bitmap, filePath: String, subPath: String): Boolean {
        return try {
            var bitmapBool = true
            val mainFile = File(filePath)

            if (!mainFile.exists())
                mainFile.mkdirs()

            FileOutputStream("$filePath/$subPath").use {
                bitmapBool = bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            bitmapBool
        } catch (ex: Exception) {
            Log.e("saveToFile", ex.toString())
            false
        }

    }


}