package com.yenen.ahmet.basecorelibrary.base.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.yenen.ahmet.basecorelibrary.base.dialog.BaseDialog
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseRxSingleHandlerViewModel

abstract class BaseRemoteReadyLoadingActivity<T, VM : BaseRxSingleHandlerViewModel<T>, DB : ViewDataBinding>(
viewModelClass: Class<VM>, private val isOnCreateGetData: Boolean
) : BaseDaggerActivity<VM, DB>(viewModelClass)  {

    private var loadingDialog: BaseDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDialog = BaseDialog(this, getDialogId())

        if (isOnCreateGetData) {
            runDataObserve(viewModel)
        }
        runErrObserve(viewModel)
        runNoDataFoundObserve(viewModel)
    }

    private fun runErrObserve(viewModel: BaseRxSingleHandlerViewModel<T>) {
        viewModel.errMessage.observe(this, Observer {
            it?.let {
                onErrorObserve(it)
                loadingDialog?.dismiss()
            }
        })
    }

    private fun runNoDataFoundObserve(viewModel: BaseRxSingleHandlerViewModel<T>) {
        viewModel.noDataFound.observe(this, Observer {
            it?.let {
                onNoDataFoundObserve(it)
                loadingDialog?.dismiss()
            }
        })
    }

    protected fun runDataObserve(viewModel: BaseRxSingleHandlerViewModel<T>) {
        preCommunicationProcedures()
        loadingDialog?.show()
        viewModel.getData().observe(this, Observer {
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

    protected fun postData(){
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
    protected open fun preCommunicationProcedures(){

    }


    @LayoutRes
    protected abstract fun getDialogId():Int

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
        loadingDialog = null
    }


}