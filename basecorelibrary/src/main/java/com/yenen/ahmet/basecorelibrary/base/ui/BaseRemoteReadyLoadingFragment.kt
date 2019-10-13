package com.yenen.ahmet.basecorelibrary.base.ui

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.yenen.ahmet.basecorelibrary.base.dialog.BaseDialog
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseRxSingleHandlerViewModel

abstract class BaseRemoteReadyLoadingFragment<T, VM : BaseRxSingleHandlerViewModel<T>, DB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int, @LayoutRes private val dialogId: Int,
    private val isOnCreateGetData: Boolean
) : BaseDaggerFragment<VM, DB>(viewModelClass, layoutRes) {

    private var loadingDialog: BaseDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadingDialog = BaseDialog(activity!!, dialogId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isOnCreateGetData) {
            runDataObserve(viewModel)
        }
        runErrObserve(viewModel)
        runNoDataFoundObserve(viewModel)
    }

    private fun runErrObserve(viewModel: BaseRxSingleHandlerViewModel<T>) {
        viewModel.errMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                onErrorObserve(it)
                loadingDialog?.dismiss()
            }
        })
    }

    private fun runNoDataFoundObserve(viewModel: BaseRxSingleHandlerViewModel<T>) {
        viewModel.noDataFound.observe(viewLifecycleOwner, Observer {
            it?.let {
                onNoDataFoundObserve(it)
                loadingDialog?.dismiss()
            }
        })
    }

    protected fun runDataObserve(viewModel: BaseRxSingleHandlerViewModel<T>) {
        preCommunicationProcedures()
        loadingDialog?.show()
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            it?.let {
                onDataObserve(it)
                loadingDialog?.dismiss()
            }
        })
    }

    protected fun runDataChangeable() {
        preCommunicationProcedures()
        loadingDialog?.show()
        viewModel.dataChangeable()
    }

    protected fun postData() {
        preCommunicationProcedures()
        loadingDialog?.show()
        viewModel.postData()
    }

    protected abstract fun onDataObserve(results: T)

    protected abstract fun onErrorObserve(errorMessage: Throwable)

    protected abstract fun onNoDataFoundObserve(noDataFoundMessage: String)

    // Eğer bir çağrı yapamadan önce bir query veya model yollanıyorsa bu fonksiyon overide edilip
    // gerekli veri viewModel üzerinde set edilir //
    // Servic çağrıları herhangi bir parametre içermiyorsa Kullanılmasına gerek yok //
    // Veya sadece bir kerelik viewModel set edilecekse, initViewModel üzerinden bu islem yapılabilir bu fonksiyonu overide etemye
    protected open fun preCommunicationProcedures() {

    }

    protected fun showDialog() {
        loadingDialog?.show()
    }

    protected fun dismissDialog() {
        loadingDialog?.dismiss()
    }

    override fun onDetach() {
        super.onDetach()
        loadingDialog?.dismiss()
        loadingDialog = null
    }


}