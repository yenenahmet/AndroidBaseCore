package com.yenen.ahmet.basecorelibrary.base.download

import android.app.DownloadManager
import android.database.Cursor

class DownloadManagerRunningListener(private val downloadManager: DownloadManager) {

    private val query = DownloadManager.Query()
    private var listener: ProgressListener? = null
    private var finishDownload = false

    fun setListener(listener: ProgressListener) {
        this.listener = listener
    }

    fun unBind() {
        this.listener = null
    }


    fun runListener(requestId: Long) {
        if (requestId > 0) {
            finishDownload = false
            while (!finishDownload) {
                query.setFilterById(requestId)
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    var isBreak = false
                    when (getStatus(cursor)) {
                        DownloadManager.STATUS_FAILED -> {
                            listener?.onProgress(0, requestId, getReason(cursor))
                            isBreak = true
                        }
                        DownloadManager.STATUS_RUNNING -> {
                            setProgress(cursor, requestId)
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            isBreak = true
                            listener?.onProgress(100, requestId, null)
                        }
                    }
                    if (isBreak) {
                        break
                    }
                }
            }
        } else {
            listener?.onProgress(0, requestId, -1)
        }
    }

    fun closeRunning() {
        finishDownload = true
    }

    private fun getStatus(cursor: Cursor): Int {
        val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        return cursor.getInt(statusIndex)
    }

    private fun getReason(cursor: Cursor): Int {
        val columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
        return cursor.getInt(columnReason)
    }

    private fun setProgress(cursor: Cursor, requestId: Long) {
        val index = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
        val total = cursor.getLong(index)
        if (total >= 0) {
            val indexS =
                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
            val downloaded = cursor.getLong(indexS)
            val progress = ((downloaded * 100L) / total).toInt()
            listener?.onProgress(progress, requestId, null)
        }
    }
}