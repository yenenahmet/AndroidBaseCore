package com.yenen.ahmet.basecorelibrary.base.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel
import java.util.*

abstract class BaseDateDaggerLoadingActivity<VM : BaseViewModel, DB : ViewDataBinding, VDB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int, @LayoutRes private val loadingLayoutResId: Int
) : BaseDaggerLoadingActivity<VM, DB, VDB>(viewModelClass, layoutRes, loadingLayoutResId),
    DatePickerDialog.OnDateSetListener {


    private var dateDialog: DatePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDateDialog(Calendar.getInstance())
    }

    private fun initDateDialog(calendar: Calendar) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dateDialog = DatePickerDialog(this, this, year, month, day)
    }

    protected fun showDatePicker() {
        dateDialog?.show()
    }

    protected fun dismissDatePicker() {
        dateDialog?.dismiss()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        onSetDate(view, year, month, dayOfMonth)
    }

    override fun onDestroy() {
        super.onDestroy()
        dateDialog?.dismiss()
        dateDialog = null
    }

    protected abstract fun onSetDate(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
}