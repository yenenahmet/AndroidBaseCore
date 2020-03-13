package com.yenen.ahmet.basecorelibrary.base.remote

import java.io.File
import java.lang.Exception

interface DownloadFileListener {
    fun onError(ex:Exception)
    fun onSuccess(fileSizeDownloaded:Long,file:File)
    fun onLoading(fileSizeDownloaded:Long)
}