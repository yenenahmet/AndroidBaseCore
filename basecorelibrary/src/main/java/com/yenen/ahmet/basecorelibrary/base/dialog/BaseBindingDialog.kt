package com.yenen.ahmet.basecorelibrary.base.dialog

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseBindingDialog<VDB : ViewDataBinding>(private var activity: Activity?, @LayoutRes private val layoutId: Int) {

    val alertDialog: AlertDialog
    var binding: VDB

    init {
        val parent = activity?.window?.decorView?.rootView as? ViewGroup
        binding = DataBindingUtil.inflate(activity?.layoutInflater!!, layoutId, parent, false)
        this.alertDialog =
            AlertDialog.Builder(activity).setView(binding.root).setCancelable(false).create()
        val color = ColorDrawable(Color.TRANSPARENT)
        val window = alertDialog.window
        window?.setBackgroundDrawable(color)
    }

    fun show() {
        activity!!.runOnUiThread { alertDialog.show() }
    }

    fun dismiss() {
        activity!!.runOnUiThread { alertDialog.dismiss() }
    }

    fun isShowing():Boolean{
        return alertDialog.isShowing
    }

    open fun unBind() {
        dismiss()
        activity = null
    }
}