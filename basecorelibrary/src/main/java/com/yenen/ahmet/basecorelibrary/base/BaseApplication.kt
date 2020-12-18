package com.yenen.ahmet.basecorelibrary.base

import android.content.Context
import android.content.res.Configuration
import com.yenen.ahmet.basecorelibrary.base.local.LocaleManager
import com.yenen.ahmet.basecorelibrary.base.local.SharedPreferencesHelper
import dagger.android.DaggerApplication


abstract class BaseApplication : DaggerApplication() {

    override fun attachBaseContext(base: Context?) {
        val sharedPreferencesHelper = SharedPreferencesHelper(base!!)
        val localeManager = LocaleManager(sharedPreferencesHelper)
        super.attachBaseContext(localeManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val localeManager = LocaleManager(sharedPreferencesHelper)
        localeManager.setLocale(this)
    }

}