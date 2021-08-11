package com.yenen.ahmet.basecorelibrary.base.extension

import com.yenen.ahmet.basecorelibrary.base.model.LiveDataResponseModel
import com.yenen.ahmet.basecorelibrary.base.ui.BaseLoadingActivity
import com.yenen.ahmet.basecorelibrary.base.ui.BaseLoadingFragment
import com.yenen.ahmet.basecorelibrary.base.utility.ProjectConstants

fun <T> LiveDataResponseModel<T>.handleOnlySuccessStatusWithLoading(
    block: (T) -> Unit, fragment: BaseLoadingFragment<*, *, *>
) {
    when (status) {
        ProjectConstants.ERROR_STATUS -> {
            fragment.dismissDialog()
            fragment.showToast(description ?: "")
        }
        ProjectConstants.SUCCESS_STATUS -> {
            fragment.dismissDialog()
            this.data?.let {
                block.invoke(it)
            }
        }
    }
}

fun <T> LiveDataResponseModel<T>.handleOnlySuccessStatusWithLoading(
    block: (T) -> Unit, activity: BaseLoadingActivity<*, *, *>
) {
    when (status) {
        ProjectConstants.ERROR_STATUS -> {
            activity.dismissDialog()
            activity.showToast(description ?: "")
        }
        ProjectConstants.SUCCESS_STATUS -> {
            activity.dismissDialog()
            this.data?.let {
                block.invoke(it)
            }
        }
    }
}