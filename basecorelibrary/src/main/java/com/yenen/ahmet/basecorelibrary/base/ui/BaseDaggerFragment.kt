package com.yenen.ahmet.basecorelibrary.base.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yenen.ahmet.basecorelibrary.base.di.factory.AppViewModelFactory
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel
import dagger.android.support.DaggerFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

abstract class BaseDaggerFragment<VM : BaseViewModel, DB : ViewDataBinding>
    (private val viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int) :
    DaggerFragment() {

    @Inject
    lateinit var providerFactory: AppViewModelFactory

    protected lateinit var binding: DB

    protected val viewModel by lazy {
        ViewModelProvider(this, providerFactory).get(viewModelClass)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,layoutRes, container, false)
        viewModel.setViewDataBinding(binding)
        initViewModel(viewModel)

        onBindingCreate(binding)

        arguments?.let {
            onBundle(it)
        }
        return binding.root
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


    protected fun showToast(text: String) {
        Toast.makeText(activity!!, text, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        onBindingClear(binding)
    }

    protected fun hideKeyboard(){
        activity?.currentFocus?.let {
            val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    protected fun startActivity(sClass: Class<*>) {
        val intent = Intent(activity, sClass)
        startActivity(intent)
    }

    protected fun startActivity(sClass: Class<*>,bundle: Bundle) {
        val intent = Intent(activity, sClass)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    protected fun reStartApp(sClass: Class<*>){
        val intent = Intent(activity!!.applicationContext, sClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity!!.finish()
        System.exit(0)
    }

    protected open fun onBundle(bundle: Bundle){

    }

}
