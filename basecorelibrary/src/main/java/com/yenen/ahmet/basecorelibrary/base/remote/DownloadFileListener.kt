package com.yenen.ahmet.basecorelibrary.base.remote

import java.lang.Exception

interface DownloadFileListener {
    fun onError(ex:Exception)
    fun onSuccess(fileSizeDownloaded:Long)
    fun onLoading(fileSizeDownloaded:Long)
}