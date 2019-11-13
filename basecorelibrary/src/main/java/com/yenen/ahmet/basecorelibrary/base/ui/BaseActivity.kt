package com.yenen.ahmet.basecorelibrary.base.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.yenen.ahmet.basecorelibrary.base.local.LocaleManager
import com.yenen.ahmet.basecorelibrary.base.local.SharedPreferencesHelper
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>(
    private val viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int
) : AppCompatActivity() {


    protected val binding by lazy {
        DataBindingUtil.setContentView(this, layoutRes) as DB
    }

    protected val viewModel by lazy {
        ViewModelProviders.of(this).get(viewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setViewDataBinding(binding)
        initViewModel(viewModel)
        onBindingCreate(binding)
    }

    /*
     *  You need override this method.
     *  And you need to set viewModel to binding: binding.viewModel = viewModel
     */
    abstract fun initViewModel(viewModel: VM)


    //  Üzerinde dinleme yapılacak tüm view ler bu fonksiyon altında kurulabilir
    // Düzenli  bir görünüm sağlamak için
    protected open fun onBindingCreate(binding: DB) {

    }

    //  Üzerinde dinleme yapılmış tüm view ler bu fonksiyon altında silinebilir
    // Düzenli  bir görünüm sağlamak için
    protected open fun onBindingClear(binding: DB) {

    }

    override fun onDestroy() {
        super.onDestroy()
        onBindingClear(binding)
    }

    override fun onPause() {
        super.onPause()
        hideKeybord()
    }

    override fun attachBaseContext(newBase: Context?) {
        val localeManager = LocaleManager(SharedPreferencesHelper(newBase!!))
        super.attachBaseContext(localeManager.setLocale(newBase))
    }


    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }


    protected fun hideKeybord() {
        currentFocus?.let {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    protected fun screenBarClear() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    protected fun startActivity(sClass: Class<*>) {
        val intent = Intent(this, sClass)
        startActivity(intent)
    }

    protected fun startActivity(sClass: Class<*>,bundle: Bundle) {
        val intent = Intent(this, sClass)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    protected fun startActivityAndFinishThis(sClass: Class<*>) {
        val intent = Intent(this, sClass)
        startActivity(intent)
        finish()
    }

    protected fun startActivityResult(sClass: Class<*>, req: Int) {
        val intent = Intent(this, sClass)
        startActivityForResult(intent, req)
    }

    protected fun setNewLocale(language :String,localeManager: LocaleManager) {
        localeManager.setNewLocale(this,language)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }

    protected fun reStartApp(sClass: Class<*>){
        val intent = Intent(applicationContext, sClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        System.exit(0)
    }
}