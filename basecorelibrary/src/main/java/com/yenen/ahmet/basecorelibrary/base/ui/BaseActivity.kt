package com.yenen.ahmet.basecorelibrary.base.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yenen.ahmet.basecorelibrary.base.extension.hideKeyboard
import com.yenen.ahmet.basecorelibrary.base.local.LocaleManager
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>(
    private val viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int
) : AppCompatActivity() {


    protected val binding by lazy {
        DataBindingUtil.setContentView(this, layoutRes) as DB
    }

    protected val viewModel by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setViewDataBinding(binding)
        initViewModel(viewModel)
        onBindingCreate(binding)
        intent.extras?.let {
            onBundle(it)
        }

        createLiveData()
        createListeners()
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

    protected fun startActivityResult(sClass: Class<*>, req: Int) {
        val intent = Intent(this, sClass)
        startActivityForResult(intent, req)
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
        System.exit(0)
    }


    protected open fun onBundle(bundle: Bundle) {

    }

    protected open fun createLiveData() {

    }

    protected open fun createListeners() {

    }


    protected fun requestPermissionsForRuntime(permissions: Array<out String>) {
        var checkSelf = true
        permissions.forEach { per->
            val result = ContextCompat.checkSelfPermission(this, per)
            if(result == PackageManager.PERMISSION_DENIED){
                checkSelf = false
            }
        }

        if(!checkSelf){
            ActivityCompat.requestPermissions(this, permissions, 1122)
        }else{
            onRequestPermissionResultForRuntime(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1122 && grantResults.isNotEmpty()){
            val size = grantResults.filter { it == PackageManager.PERMISSION_GRANTED }.size
            if(size == grantResults.size){
                onRequestPermissionResultForRuntime(true)
            }else{
                onRequestPermissionResultForRuntime(false)
            }
        }

    }

    protected open fun onRequestPermissionResultForRuntime(isGranted:Boolean){

    }


}