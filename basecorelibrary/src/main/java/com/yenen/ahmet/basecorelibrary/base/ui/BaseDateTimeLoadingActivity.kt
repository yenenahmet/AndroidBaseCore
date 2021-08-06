package com.yenen.ahmet.basecorelibrary.base.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import java.util.Calendar

abstract class BaseDateTimeLoadingActivity <VM : ViewModel, DB : ViewDataBinding, VDB : ViewDataBinding>(
    viewModelClass: Class<VM>,@LayoutRes private val loadingLayoutResId: Int
) : BaseLoadingActivity<VM, DB, VDB>(viewModelClass, loadingLayoutResId),
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var dateDialog: DatePickerDialog? = null
    private var dateListener: DatePickerDialog.OnDateSetListener? = null
    private var timeDialog: TimePickerDialog? = null
    private var timeListener: TimePickerDialog.OnTimeSetListener? = null
    private var date: String = ""

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

    private fun showTimePicker() {
        timeDialog?.show()
    }

    protected fun dismissTimePicker() {
        timeDialog?.dismiss()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val mMonth:String = if(month+1>9){
            month.toString()
        }else{
            "0${month+1}"
        }

        val mDay:String = if(dayOfMonth>9){
            dayOfMonth.toString()
        }else{
            "0${dayOfMonth}"
        }
        date = "$year-$mMonth-${mDay}T"
        showTimePicker()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val mHours:String = if(hourOfDay>9){
            hourOfDay.toString()
        }else{
            "0${hourOfDay}"
        }
        val mMinute:String = if(minute>9){
            minute.toString()
        }else{
            "0${minute}"
        }
        val dateTime = "${date}${mHours}:${mMinute}:00.000"
        onDateTime(dateTime)
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

    protected abstract fun onDateTime(dateTime: String)
}