package com.yenen.ahmet.basecorelibrary.base.utility

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import kotlin.math.roundToInt

object ImageScaleUtils {

    fun getScaledImage(
        targetLength: Int, quality: Int, compressFormat: Bitmap.CompressFormat,
        outputDirPath: String, outputFilename: String?, sourceImage: File
    ): File {
        val directory = File(outputDirPath)
        if (!directory.exists()) {
            directory.mkdir()
        }
        val outputFilePath =
            getOutputFilePath(compressFormat, outputDirPath, outputFilename, sourceImage)
        val scaledBitmap = getScaledBitmap(targetLength, sourceImage)
        writeBitmapToFile(scaledBitmap, compressFormat, quality, outputFilePath)
        return File(outputFilePath)
    }


    fun getOutputFilePath(
        compressFormat: Bitmap.CompressFormat,
        outputDirPath: String, outputFilename: String?, sourceImage: File
    ): String {
        val originalFileName = sourceImage.name

        val targetFileExtension = ".${
            compressFormat.name.toLowerCase(Locale.US).replace("jpeg", "jpg")
        }"
        val targetFileName = if (outputFilename == null) {
            val extensionIndex = originalFileName.lastIndexOf('.')
            if (extensionIndex == -1) {
                originalFileName + targetFileExtension
            } else {
                originalFileName.substring(0, extensionIndex) + targetFileExtension
            }
        } else {
            outputFilename + targetFileExtension
        }
        return outputDirPath + File.separator + targetFileName
    }

    fun getScaledBitmap(targetLength: Int, sourceImage: File): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = false
        var bitmap = BitmapFactory.decodeFile(sourceImage.absolutePath, options)
        try {
            val ei = ExifInterface(sourceImage.absolutePath)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    bitmap = rotateImage(bitmap, 90)
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    bitmap = rotateImage(bitmap, 180)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    bitmap = rotateImage(bitmap, 270)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var aspectRatio: Float = originalWidth.toFloat() / originalHeight.toFloat()
        var targetWidth: Int = 0
        var targetHeight: Int = 0

        if (originalWidth > originalHeight) {
            aspectRatio = 1 / aspectRatio;
            targetHeight = targetLength;
            targetWidth = (targetHeight / aspectRatio).roundToInt()
        } else {
            targetWidth = targetLength;
            targetHeight = (targetWidth / aspectRatio).roundToInt()
        }

        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    fun writeBitmapToFile(
        bitmap: Bitmap,
        compressFormat: Bitmap.CompressFormat,
        quality: Int,
        filePath: String
    ) {
        FileOutputStream(filePath).use {
            bitmap.compress(compressFormat, quality, it)
            it.flush()
        }
    }
}