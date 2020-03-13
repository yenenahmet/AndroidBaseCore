package com.yenen.ahmet.basecorelibrary.base.remote

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class DownloadFile constructor(
    private val inputStream: InputStream,
    private val path: String,
    private val subPath: String,
    private val listener: DownloadFileListener
) {

    fun writeInputStreamToDisk() {
        try {
            val filePath = File(path)
            val file = File("${filePath.absolutePath}/$subPath")

            if (!filePath.exists())
                filePath.mkdirs()

            var fileSizeDownloaded = 0L


            val fileReader = ByteArray(4096)
            FileOutputStream(file).use {
                inputStream.use { inputStream ->
                    while (true) {
                        val read = inputStream.read(fileReader)

                        if (read == -1)
                            break

                        it.write(fileReader, 0, read)

                        fileSizeDownloaded += read
                        listener.onLoading(fileSizeDownloaded)
                    }
                }
                it.flush()
            }
            listener.onSuccess(fileSizeDownloaded,file.absolutePath)
        } catch (ex: Exception) {
            listener.onError(ex)
        }

    }
}