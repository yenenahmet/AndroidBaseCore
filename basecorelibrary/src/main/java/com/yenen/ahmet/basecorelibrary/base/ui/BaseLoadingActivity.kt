package com.yenen.ahmet.basecorelibrary.base.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.yenen.ahmet.basecorelibrary.base.dialog.BaseBindingDialog

abstract class BaseLoadingActivity<VM : ViewModel, DB : ViewDataBinding, VDB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int, @LayoutRes private val loadingLayoutResId: Int
) : BaseActivity<VM, DB>(viewModelClass, layoutRes) {

    private var loadingDialog: BaseBindingDialog<VDB>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = BaseBindingDialog(this, loadingLayoutResId)
    }


    fun showDialog() {
        loadingDialog?.show()
    }

    fun isShowing():Boolean{
        return loadingDialog?.isShowing()?: false
    }

    fun dismissDialog() {
        loadingDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
        loadingDialog = null
    }

}