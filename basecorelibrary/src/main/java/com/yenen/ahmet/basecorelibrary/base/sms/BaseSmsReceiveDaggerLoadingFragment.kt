package com.yenen.ahmet.basecorelibrary.base.sms

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Telephony
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.yenen.ahmet.basecorelibrary.base.dialog.BaseBindingDialog
import com.yenen.ahmet.basecorelibrary.base.ui.BaseDaggerFragment
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel

abstract class BaseSmsReceiveDaggerLoadingFragment<VM : BaseViewModel, DB : ViewDataBinding, VDB : ViewDataBinding>
    (
    private val viewModelClass: Class<VM>,
    @LayoutRes private val layoutRes: Int,
    @LayoutRes private val loadingLayoutResId: Int,
    private val serviceProviderNumber: String
) :
    BaseDaggerFragment<VM, DB>(viewModelClass, layoutRes) , SmsListener.Listener{

    private var smsListener: SmsListener? = null
    private var loadingDialog: BaseBindingDialog<VDB>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        smsListener = SmsListener(serviceProviderNumber)
        val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        activity?.registerReceiver(smsListener, filter)
    }

    override fun onTextReceived(text: String) {
        onTextSmsReceived(text)
    }

    protected abstract fun onTextSmsReceived(text: String)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadingDialog = BaseBindingDialog(activity, loadingLayoutResId)
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
        smsListener?.let {
            activity?.unregisterReceiver(it)
            it.setListener(null)
        }
        smsListener = null
    }
}