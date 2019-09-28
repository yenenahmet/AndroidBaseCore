package com.yenen.ahmet.basecorelibrary.base.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseRxSingleHandlerViewModel

// Servisten  bir çağrı yapıp.Geri dönüşü bekledikten sonra,
// eğer data dönerse ekrana basma, boş dönüş olduğunda veya hata olduğunda kullanıcıya bu durumu ifade
// etmek için oluşturulmuştur.
// Amaç: Her seferinde bu işlemlerin tekrarını önlenmek ve  projenin gidiş hattını standartlaştırmak.
// Loading işleminin ve livedata yönetimini doğru bir şekilde sağlamak.
abstract class BaseRemoteFragment<T, VM : BaseRxSingleHandlerViewModel<T>, DB : ViewDataBinding>(
    viewModelClass: Class<VM>, private val isOnCreateGetData: Boolean
) : BaseDaggerFragment<VM, DB>(viewModelClass) {



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
                onCommunicationCompleted()
            }
        })
    }

    private fun runNoDataFoundObserve(viewModel: BaseRxSingleHandlerViewModel<T>) {
        viewModel.noDataFound.observe(viewLifecycleOwner, Observer {
            it?.let {
                onNoDataFoundObserve(it)
                onCommunicationCompleted()
            }
        })
    }

    protected fun runDataObserve(viewModel: BaseRxSingleHandlerViewModel<T>) {
        preCommunicationProcedures()
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            it?.let {
                onDataObserve(it)
                onCommunicationCompleted()
            }
        })
    }

    protected fun runDataChangeable() {
        preCommunicationProcedures()
        viewModel.dataChangeable()
    }

    protected abstract fun onDataObserve(results: T)

    protected abstract fun onErrorObserve(errorMessage: Throwable)

    protected abstract fun onNoDataFoundObserve(noDataFoundMessage: String)

    // Eğer bir çağrı yapamadan önce bir query veya model yollanıyorsa bu fonksiyon overide edilip
    // gerekli veri viewModel üzerinde set edilir // Veya Loading Bunun altında başlatılabilir //
    protected abstract fun preCommunicationProcedures()

    protected abstract fun onCommunicationCompleted()
}