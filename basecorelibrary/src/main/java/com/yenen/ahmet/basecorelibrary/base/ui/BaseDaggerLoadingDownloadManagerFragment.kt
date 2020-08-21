package com.yenen.ahmet.basecorelibrary.base.ui

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.core.content.FileProvider
import androidx.databinding.ViewDataBinding
import com.yenen.ahmet.basecorelibrary.base.download.CompleteListener
import com.yenen.ahmet.basecorelibrary.base.download.DownloadManagerListener
import com.yenen.ahmet.basecorelibrary.base.utility.FileUtils
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel
import java.io.File
import java.lang.Exception

abstract class BaseDaggerLoadingDownloadManagerFragment<VM : BaseViewModel, DB : ViewDataBinding, VDB : ViewDataBinding>(
    viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int, @LayoutRes private val loadingLayoutResId: Int
) :
    BaseDaggerLoadingFragment<VM, DB, VDB>(viewModelClass, layoutRes, loadingLayoutResId),
    CompleteListener {

    private var downloadManagerListener: DownloadManagerListener? = null

    override fun onResult(
        status: Int,
        reason: Int,
        requestId: Long,
        uri: Uri?,
        mimeType: String,
        notificationVisibility: Int,
        title: String
    ) {
        removeDownloadRequestId(requestId)
        onCompleted(status, reason, requestId, uri, mimeType, "", notificationVisibility, title)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        clearRegister()
        createRegister()
    }

    private fun createRegister() {
        val downloadManager =
            activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManagerListener = DownloadManagerListener(downloadManager)
        downloadManagerListener?.setListener(this)

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        activity?.registerReceiver(downloadManagerListener, filter)
    }

    private fun clearRegister() {
        downloadManagerListener?.let {
            activity?.unregisterReceiver(it)
            it.unBind()
        }
    }


    private fun addDownloadRequestId(requestId: Long, notificationVisibility: Int) {
        downloadManagerListener?.addRequestIds(requestId, notificationVisibility)
    }

    private fun removeDownloadRequestId(requestId: Long) {
        downloadManagerListener?.removeRequestIds(requestId)
    }


    protected fun runDownloadManager(
        notificationVisibility: Int,
        path: String,
        fileName: String,
        token: String?,
        downloadAgain: Boolean,
        authority: String
    ) {
        try {
            if (downloadAgain) {
                download(notificationVisibility, path, fileName, token)
            } else {
                val file = getFile(fileName)
                if (!file.exists()) {
                    download(notificationVisibility, path, fileName, token)
                } else {
                    val uri =
                        FileProvider.getUriForFile(
                            activity!!,
                            authority,
                            file
                        )


                    var mimeType = FileUtils.getMimeType(fileName)
                    if (mimeType == "*/*") {
                        mimeType = FileUtils.getMimeType(file.name)
                        if (mimeType == "*/*") {
                            mimeType = FileUtils.getExtensionMimeType(file.name)
                        }
                    }

                    onCompleted(0, 0, 0, uri, mimeType, "", notificationVisibility, fileName)
                }
            }
        } catch (ex: Exception) {
            onCompleted(-1, -1, -1, null, "", ex.toString(), notificationVisibility, fileName)
        }
    }

    // Visibility == VISIBLE // It haven't token//
    protected fun runDownloadManager(path: String, fileName: String) {
        runDownloadManager(
            DownloadManager.Request.VISIBILITY_VISIBLE,
            path,
            fileName,
            null,
            true,
            ""
        )
    }

    // Visibility == VISIBLE // It have token//
    protected fun runDownloadManager(path: String, fileName: String, token: String) {
        runDownloadManager(
            DownloadManager.Request.VISIBILITY_VISIBLE,
            path,
            fileName,
            token,
            true,
            ""
        )
    }

    private fun download(
        notificationVisibility: Int,
        path: String,
        fileName: String,
        token: String?
    ) {
        val request = DownloadManager.Request(Uri.parse(path)).apply {
            setTitle(fileName)
            setDescription("Dosya indiriliyor...")
            setNotificationVisibility(notificationVisibility)
            setDestinationInExternalFilesDir(activity, null, fileName)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            token?.let {
                addRequestHeader("Authorization", it)
            }
        }
        val downloadManager =
            activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val req = downloadManager.enqueue(request)
        addDownloadRequestId(req, notificationVisibility)
    }

    protected fun getFile(fileName: String): File {
        return File(activity?.getExternalFilesDir(null), fileName)
    }

    protected abstract fun onCompleted(
        status: Int,
        reason: Int,
        requestId: Long,
        uri: Uri?,
        mimeType: String,
        err: String,
        notificationVisibility: Int,
        title: String
    )

    override fun onDetach() {
        super.onDetach()
        clearRegister()
    }

}