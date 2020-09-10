package com.yenen.ahmet.basecorelibrary.base.ui


import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yenen.ahmet.basecorelibrary.base.di.factory.AppViewModelFactory
import com.yenen.ahmet.basecorelibrary.base.extension.hideKeyboard
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

//  Her Activity Üzerinde tekrar edebilecek yapılar bu class altından toplanmıştır.
//  AppViewModelFactory her seferinde yazılması kaldırılmıştır
// Binding nerede ve nasıl kullanacağı standartlaştırılmıştır
// binding ve viewmodel  bu sınıf içinde ayarlanmıştır.
abstract class BaseDaggerActivity<VM : BaseViewModel, DB : ViewDataBinding>(
    private val viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int
) : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: AppViewModelFactory

    protected val binding by lazy {
        DataBindingUtil.setContentView(this, layoutRes) as DB
    }

    protected val viewModel by lazy {
        ViewModelProvider(this, factory).get(viewModelClass)
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

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        onBindingClear(binding)
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

    protected fun reStartApp(sClass: Class<*>) {
        val intent = Intent(applicationContext, sClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        System.exit(0)
    }

    protected open fun onBundle(bundle: Bundle) {

    }


    protected open fun createLiveData( ){

    }

    protected open fun createListeners(){

    }
}
