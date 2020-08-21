package com.yenen.ahmet.basecorelibrary.base.download

import android.net.Uri

interface CompleteListener {
    fun onResult(status:Int,reason:Int,requestId:Long,uri:Uri?,mimeType:String,notificationVisibility:Int,title:String)
}