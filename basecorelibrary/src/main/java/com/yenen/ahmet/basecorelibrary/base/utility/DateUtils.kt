package com.yenen.ahmet.basecorelibrary.base.utility

import android.annotation.SuppressLint
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    val monthNames =
        arrayOf("Oca", "Şub", "Mar", "Nis", "May", "Haz", "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara")

    val dfServer = "yyyy-MM-dd'T'HH:mm:ss.SSS"

    @SuppressLint("SimpleDateFormat")
    fun getServerDateTimeFormatShort(dateTime: String): String {
        try {
            val simpleDateFormat = SimpleDateFormat(dfServer)
            val date = simpleDateFormat.parse(dateTime)
            val cal = Calendar.getInstance()
            cal.time = date!!
            val day = cal.get(Calendar.DAY_OF_MONTH).toString()
            val mont = monthNames[cal.get(Calendar.MONTH)]
            val time = "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}"
            return "$day $mont $time"
        } catch (ex: Exception) {
            return dateTime
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun compareToDay(dateTime: String):Int{
        try{
            val simpleDateFormat = SimpleDateFormat(dfServer)
            val date = simpleDateFormat.parse(dateTime)
            val now = Date()
            return now.compareTo(date)
        }catch (ex:Exception){
            return -2
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getNowDate():String{
        val dateFormat = SimpleDateFormat(dfServer)
        return dateFormat.format(Date())
    }
}