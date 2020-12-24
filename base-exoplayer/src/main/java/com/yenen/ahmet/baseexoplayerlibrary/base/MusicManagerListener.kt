package com.yenen.ahmet.baseexoplayerlibrary.base

interface MusicManagerListener {
    fun onError(ex:Exception)
    fun onProgress(currentPosition:String,duration:String,progress:Int,mCurrentPosition: Long,mDuration:Long)
    fun onLoading()
    fun onContinues()
    fun onFinish()
}