package com.yenen.ahmet.basecorelibrary.base.ui

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.yenen.ahmet.basecorelibrary.base.dialog.BaseBindingDialog

abstract class BaseLoadingFragment<VM : ViewModel, DB : ViewDataBinding, VDB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int, @LayoutRes private val loadingLayoutResId: Int
) : BaseFragment<VM, DB>(viewModelClass, layoutRes) {

    private var loadingDialog: BaseBindingDialog<VDB>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadingDialog = BaseBindingDialog(activity, loadingLayoutResId)
    }

    protected fun showDialog() {
        loadingDialog?.show()
    }

    protected fun dismissDialog() {
        loadingDialog?.dismiss()
    }

    protected fun isShowing():Boolean{
        return loadingDialog?.isShowing()?: false
    }

    override fun onDetach() {
        super.onDetach()
        loadingDialog?.dismiss()
        loadingDialog = null
    }


}