package com.yenen.ahmet.basecorelibrary.base.sms

import android.content.IntentFilter
import android.os.Bundle
import android.provider.Telephony
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.yenen.ahmet.basecorelibrary.base.ui.BaseRemoteReadyLoadingActivity
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseRxSingleHandlerViewModel

abstract class BaseSmsReceiveRemoteReadyLoadingActivity<T, VM : BaseRxSingleHandlerViewModel<T>, DB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int, @LayoutRes private val dialogId: Int,
    isOnCreateGetData: Boolean, private val serviceProviderNumber: String
) : BaseRemoteReadyLoadingActivity<T, VM, DB>(
    viewModelClass,
    layoutRes,
    dialogId,
    isOnCreateGetData
), SmsListener.Listener {

    private var smsListener: SmsListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        smsListener = SmsListener(serviceProviderNumber)
        val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        registerReceiver(smsListener, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        smsListener?.let {
            unregisterReceiver(it)
            it.setListener(null)
        }
        smsListener = null
    }

    override fun onTextReceived(text: String) {
        onTextSmsReceived(text)
    }

    protected abstract fun onTextSmsReceived(text: String)

}