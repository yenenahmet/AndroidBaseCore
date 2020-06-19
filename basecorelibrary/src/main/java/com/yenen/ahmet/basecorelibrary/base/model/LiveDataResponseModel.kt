package com.yenen.ahmet.basecorelibrary.base.model

class LiveDataResponseModel<T>(t: T?, mStatus: Int,mDescription:String?) {
    val data: T? = t
    val status = mStatus
    val description:String? = mDescription
}