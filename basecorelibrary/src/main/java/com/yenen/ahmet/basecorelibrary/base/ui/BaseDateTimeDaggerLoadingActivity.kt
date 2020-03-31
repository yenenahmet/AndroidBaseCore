package com.yenen.ahmet.basecorelibrary.base.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel
import java.util.*


abstract class BaseDateTimeDaggerLoadingActivity<VM : BaseViewModel, DB : ViewDataBinding, VDB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int, @LayoutRes private val loadingLayoutResId: Int
) : BaseDaggerLoadingActivity<VM, DB, VDB>(viewModelClass, layoutRes, loadingLayoutResId),
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var dateDialog: DatePickerDialog? = null
    private var dateListener: DatePickerDialog.OnDateSetListener? = null
    private var timeDialog: TimePickerDialog? = null
    private var timeListener: TimePickerDialog.OnTimeSetListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cal = Calendar.getInstance()
        initDateDialog(cal)
        initTimeDialog(cal)
    }

    private fun initDateDialog(calendar: Calendar) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dateListener = this
        dateDialog = DatePickerDialog(this, dateListener, year, month, day)
    }

    private fun initTimeDialog(calendar: Calendar) {
        timeListener = this
        timeDialog =
            TimePickerDialog(
                this,
                timeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
    }

    protected fun showDatePicker() {
        dateDialog?.show()
    }

    protected fun dismissDatePicker() {
        dateDialog?.dismiss()
    }

    protected fun showTimePicker(){
        timeDialog?.show()
    }

    protected fun dismissTimePicker(){
        timeDialog?.dismiss()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        onSetDate(view, year, month, dayOfMonth)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        onSetTime(view, hourOfDay, minute)
    }

    override fun onDestroy() {
        super.onDestroy()
        dateDialog?.dismiss()
        dateDialog = null
        dateListener = null
        timeDialog?.dismiss()
        timeDialog = null
        timeListener = null
    }

    protected abstract fun onSetDate(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)

    protected abstract fun onSetTime(view: TimePicker?, hourOfDay: Int, minute: Int)
}