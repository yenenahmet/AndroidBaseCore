package com.yenen.ahmet.basecorelibrary.base.ui

import android.content.Context
import android.os.Bundle
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
    private val viewModelClass: Class<VM>
) : AppCompatActivity() {

    @LayoutRes
    abstract fun getLayoutRes(): Int


    protected val binding by lazy {
        DataBindingUtil.setContentView(this, getLayoutRes()) as DB
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


    protected fun hideKeybord(){
        currentFocus?.let {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}