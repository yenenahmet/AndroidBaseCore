package com.yenen.ahmet.basecorelibrary.base.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<VM : ViewModel, DB : ViewDataBinding>(
    private val viewModelClass: Class<VM>
) : Fragment() {

    protected val viewModel by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    protected lateinit var binding: ViewDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = getViewDataBinding(inflater, container)

        initViewModel(viewModel)

        onBindingCreate(binding as DB)

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

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        onBindingClear(binding as DB)
    }

    protected fun hideKeyboard() {
        activity?.currentFocus?.let {
            val inputManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }


    protected fun startActivity(sClass: Class<*>) {
        val intent = Intent(activity, sClass)
        startActivity(intent)
    }

    protected fun startActivity(sClass: Class<*>, bundle: Bundle) {
        val intent = Intent(activity, sClass)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    protected fun reStartApp(sClass: Class<*>) {
        val intent = Intent(activity?.applicationContext, sClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
        System.exit(0)
    }

    protected open fun onBundle(bundle: Bundle) {

    }

    protected abstract fun getViewDataBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?
    ): ViewDataBinding

}
