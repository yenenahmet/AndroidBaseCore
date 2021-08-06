package com.yenen.ahmet.basecorelibrary.base.ui

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseIsoDepActivity<VM : ViewModel, DB : ViewDataBinding>(
    viewModelClass: Class<VM>
) : BaseActivity<VM, DB>(viewModelClass) {

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { nt ->
            if (NfcAdapter.ACTION_TECH_DISCOVERED == nt.action) {
                val tag = nt.extras?.getParcelable<Tag>(NfcAdapter.EXTRA_TAG)
                tag?.techList?.let {
                    if (it.contains("android.nfc.tech.IsoDep")) {
                        onNewIntent(IsoDep.get(tag))
                    }
                }
            }
        }
    }

    open fun onNewIntent(isoDep: IsoDep) {

    }

    fun disableNfc() {
        try {
            nfcAdapter?.disableForegroundDispatch(this)
        } catch (ex: Exception) {
        }
    }

    fun activeNfc() {
        val intent = Intent(applicationContext, this.javaClass).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val filter = arrayOf(arrayOf(IsoDep::class.java.name))
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        disableNfc()
    }
}