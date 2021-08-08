package com.yenen.ahmet.basecorelibrary.base.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yenen.ahmet.basecorelibrary.base.extension.hideKeyboard
import com.yenen.ahmet.basecorelibrary.base.local.LocaleManager
import kotlin.system.exitProcess

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<VM : ViewModel, DB : ViewDataBinding>(
    private val viewModelClass: Class<VM>
) : AppCompatActivity() {

    protected lateinit var binding: DB

    protected val viewModel by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        initViewModel(viewModel)
        onBindingCreate(binding)
        intent.extras?.let {
            onBundle(it)
        }
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
        hideKeyboard()
    }


    protected fun startActivity(sClass: Class<*>) {
        val intent = Intent(this, sClass)
        startActivity(intent)
    }

    protected fun startActivity(sClass: Class<*>, bundle: Bundle) {
        val intent = Intent(this, sClass)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    protected fun startActivityAndFinishThis(sClass: Class<*>) {
        val intent = Intent(this, sClass)
        startActivity(intent)
        finish()
    }

    protected fun setNewLocale(language: String, localeManager: LocaleManager) {
        localeManager.setNewLocale(this, language)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }

    protected fun reStartApp(sClass: Class<*>) {
        val intent = Intent(applicationContext, sClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        exitProcess(0)
    }

    protected open fun onBundle(bundle: Bundle) {

    }

    protected abstract fun getViewBinding(): DB
}
