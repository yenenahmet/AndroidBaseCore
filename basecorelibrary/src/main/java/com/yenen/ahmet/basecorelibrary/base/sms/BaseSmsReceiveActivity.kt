package com.yenen.ahmet.basecorelibrary.base.sms

import android.content.IntentFilter
import android.os.Bundle
import android.provider.Telephony
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.yenen.ahmet.basecorelibrary.base.ui.BaseActivity

abstract class BaseSmsReceiveActivity<VM : ViewModel, DB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int,
    private val serviceProviderNumber: String
) : BaseActivity<VM, DB>(viewModelClass, layoutRes), SmsListener.Listener {


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