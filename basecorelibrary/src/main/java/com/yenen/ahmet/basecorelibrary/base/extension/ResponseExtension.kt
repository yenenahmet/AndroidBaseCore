package com.yenen.ahmet.basecorelibrary.base.extension

import com.yenen.ahmet.basecorelibrary.base.model.LiveDataResponseModel
import com.yenen.ahmet.basecorelibrary.base.ui.BaseLoadingFragment
import com.yenen.ahmet.basecorelibrary.base.utility.ProjectConstants

fun <T> LiveDataResponseModel<T>.handleOnlySuccessStatusWithLoading(
    block: (T?) -> Unit, fragment: BaseLoadingFragment<*, *, *>
) {
    when (status) {
        ProjectConstants.ERROR_STATUS -> {
            fragment.showToast(description ?: "")
            fragment.dismissDialog()
        }
        ProjectConstants.SUCCESS_STATUS -> {
            block.invoke(this.data)
            fragment.dismissDialog()
        }
    }
}