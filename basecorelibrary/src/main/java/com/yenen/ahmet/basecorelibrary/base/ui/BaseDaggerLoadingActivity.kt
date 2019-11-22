package com.yenen.ahmet.basecorelibrary.base.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.yenen.ahmet.basecorelibrary.base.dialog.BaseBindingDialog
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel

abstract class BaseDaggerLoadingActivity<VM : BaseViewModel, DB : ViewDataBinding,VDB:ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int, @LayoutRes private val loadingLayoutResId: Int
) : BaseDaggerActivity<VM, DB>(viewModelClass, layoutRes) {

    private var loadingDialog: BaseBindingDialog<VDB>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = BaseBindingDialog(this, loadingLayoutResId)
    }


    protected fun showDialog() {
        loadingDialog?.show()
    }

    protected fun dismisDialog() {
        loadingDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
        loadingDialog = null
    }

}