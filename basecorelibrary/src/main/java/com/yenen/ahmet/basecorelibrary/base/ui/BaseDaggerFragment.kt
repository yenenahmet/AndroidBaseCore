package com.yenen.ahmet.basecorelibrary.base.ui


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.yenen.ahmet.basecorelibrary.base.di.factory.AppViewModelFactory
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel
import dagger.android.support.DaggerFragment
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createLiveData(viewLifecycleOwner)
        createListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,layoutRes, container, false)
        onBaseCreateView(inflater, container, savedInstanceState)
        viewModel.setViewDataBinding(binding)
        initViewModel(viewModel)

        onBindingCreate(binding)

        arguments?.let {
            onBundle(it)
        }
        return binding.root
    }


    open fun onBaseCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?){

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

    protected open fun createLiveData(lifecycleOwner: LifecycleOwner){

    }

    protected open fun createListeners(){

    }





    protected fun requestPermissionsForRuntime(permissions: Array<out String>) {
        activity?.let {
            var checkSelf = true
            permissions.forEach { per->
                val result = ContextCompat.checkSelfPermission(it, per)
                if(result == PackageManager.PERMISSION_DENIED){
                    checkSelf = false
                }
            }

            if(!checkSelf){
                ActivityCompat.requestPermissions(it, permissions, 1122)
            }else{
                onRequestPermissionResultForRuntime(true)
            }
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
