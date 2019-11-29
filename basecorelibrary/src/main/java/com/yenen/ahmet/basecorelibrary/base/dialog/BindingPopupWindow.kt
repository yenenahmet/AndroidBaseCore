package com.yenen.ahmet.basecorelibrary.base.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class BindingPopupWindow<VDB : ViewDataBinding> constructor(
    @LayoutRes private val layoutRes: Int, context: Context
) {

    private val popupBinding: VDB = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        layoutRes,
        null, false
    )

    private val popupWindow: PopupWindow

    init {
        popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun showPopup(view: View) {
        popupWindow.showAsDropDown(view)
    }

    fun showPopup(view: View, xoff: Int, yoff: Int) {
        popupWindow.showAsDropDown(view, xoff, yoff)
    }


    fun showPopup(view: View, xoff: Int, yoff: Int, gravity: Int) {
        popupWindow.showAsDropDown(view, xoff, yoff, gravity)
    }

    fun dismiss() {
        popupWindow.dismiss()
    }

    fun getBinding(): VDB {
        return popupBinding
    }

    fun getPopupWindow(): PopupWindow {
        return popupWindow
    }

}