package com.yenen.ahmet.baseexoplayerlibrary.base

interface ExoPlayerFullScreenDialogListener {
    fun onLoading()
    fun onError(ex:Exception)
    fun onContinues()
    fun onFinish()
}