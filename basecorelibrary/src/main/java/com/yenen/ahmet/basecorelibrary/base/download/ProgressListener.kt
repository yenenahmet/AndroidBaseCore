package com.yenen.ahmet.basecorelibrary.base.download

interface ProgressListener {
    fun onProgress(value:Int,id:Long,failReason:Int?)
}