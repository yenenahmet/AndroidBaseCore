package com.yenen.ahmet.basecorelibrary.base.local

import android.content.Context

class SharedPreferencesHelper constructor(context: Context) {
    private val sharedPref = context.getSharedPreferences("SSSSS_BASE", Context.MODE_PRIVATE)


    fun addValueApply(keyValue: String, stringValue: String){
        val editor = sharedPref.edit()
        editor.putString(keyValue, stringValue)
        editor.apply()
    }


    fun addValueApply(keyValue: String, intValue: Int){
        val editor = sharedPref.edit()
        editor.putInt(keyValue, intValue)
        editor.apply()
    }

    fun addValueApply(keyValue: String, floatValue: Float){
        val editor = sharedPref.edit()
        editor.putFloat(keyValue, floatValue)
        editor.apply()
    }


    fun addValueApply(keyValue: String, booleanValue: Boolean ){
        val editor = sharedPref.edit()
        editor.putBoolean(keyValue, booleanValue)
        editor.apply()
    }


    fun addValueApply(keyValue: String, longValue: Long ){
        val editor = sharedPref.edit()
        editor.putLong(keyValue, longValue)
        editor.apply()
    }

    fun addValueApply(keyValue: String, setStringValue: Set<String> ){
        val editor = sharedPref.edit()
        editor.putStringSet(keyValue, setStringValue)
        editor.apply()
    }


    fun getValue(keyValue: String,defaultValue:String){
        sharedPref.getString(keyValue,defaultValue)
    }

    fun getValue(keyValue: String,defaultValue:Int){
        sharedPref.getInt(keyValue,defaultValue)
    }


    fun getValue(keyValue: String,defaultValue:Long){
        sharedPref.getLong(keyValue,defaultValue)
    }

    fun getValue(keyValue: String,defaultValue:Boolean){
        sharedPref.getBoolean(keyValue,defaultValue)
    }

    fun getValue(keyValue: String,defaultValue:Float){
        sharedPref.getFloat(keyValue,defaultValue)
    }

    fun getValue(keyValue: String,defaultValue:Set<String>){
        sharedPref.getStringSet(keyValue,defaultValue)
    }


    fun clearPreferences(){
        sharedPref.edit().let {
            it.clear()
            it.apply()
        }
    }

    fun removeKey(keyValue: String){
        sharedPref.edit().let {
            it.remove(keyValue)
            it.apply()
        }
    }
}