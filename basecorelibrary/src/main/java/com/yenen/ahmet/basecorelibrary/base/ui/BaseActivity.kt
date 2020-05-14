package com.yenen.ahmet.basecorelibrary.base.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yenen.ahmet.basecorelibrary.base.local.LocaleManager
import com.yenen.ahmet.basecorelibrary.base.local.SharedPreferencesHelper
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>(
    private val viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int
) : AppCompatActivity() {


    protected val binding by lazy {
        DataBindingUtil.setContentView(this, layoutRes) as DB
    }

    protected val viewModel by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setViewDataBinding(binding)
        initViewModel(viewModel)
        onBindingCreate(binding)
        intent.extras?.let {
            onBundle(it)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        onBindingClear(binding)
    }

    override fun onPause() {
        super.onPause()
        hideKeybord()
    }

    override fun attachBaseContext(newBase: Context?) {
        val localeManager = LocaleManager(SharedPreferencesHelper(newBase!!))
        super.attachBaseContext(localeManager.setLocale(newBase))
    }


    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }


    protected fun hideKeybord() {
        currentFocus?.let {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    protected fun screenBarClear() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    protected fun startActivity(sClass: Class<*>) {
        val intent = Intent(this, sClass)
        startActivity(intent)
    }

    protected fun startActivity(sClass: Class<*>, bundle: Bundle) {
        val intent = Intent(this, sClass)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    protected fun startActivityAndFinishThis(sClass: Class<*>) {
        val intent = Intent(this, sClass)
        startActivity(intent)
        finish()
    }

    protected fun startActivityResult(sClass: Class<*>, req: Int) {
        val intent = Intent(this, sClass)
        startActivityForResult(intent, req)
    }

    protected fun setNewLocale(language: String, localeManager: LocaleManager) {
        localeManager.setNewLocale(this, language)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }

    protected fun reStartApp(sClass: Class<*>) {
        val intent = Intent(applicationContext, sClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        System.exit(0)
    }


    protected open fun onBundle(bundle: Bundle) {

    }

    protected fun openFile(uri: Uri, fileType: String, title: String): Boolean {
        val target = Intent(Intent.ACTION_VIEW)
        target.setDataAndType(uri, fileType)
        target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val intent = Intent.createChooser(target, title)
        return try {
            startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }


    protected fun shareFile(
        fileType: String,
        file: File,
        filUri: Uri,
        subject: String,
        chooserTitle: String
    ): Boolean {

        if (!file.exists()) {
            return false
        }

        val intentShareFile = Intent().apply {
            type = fileType
            putExtra(Intent.EXTRA_STREAM, filUri)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        Intent.createChooser(intentShareFile, chooserTitle)?.let {
            startActivity(it)
            return true
        }
        return false

    }


    protected fun openAppPermissionPage(): Boolean {
        return try {
            val intent = Intent().apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
            true
        } catch (ex: Exception) {
            false
        }
    }


    protected fun getMediaPath(uri: Uri?, mProjection: String): String? {
        uri?.let {
            val projection = arrayOf(mProjection)
            var path: String? = null
            contentResolver.query(it, projection, null, null, null)?.use {cursor->
                val idColumn  = cursor.getColumnIndexOrThrow(mProjection)
                cursor.moveToFirst()
                val contentUri: Uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getString(idColumn)
                )
                path = contentUri.toString()
            }
            return path
        }
        return null
    }

    protected fun openForResultMediaImage(title: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, title), requestCode)
    }

    protected fun openForResultMediaVideo(title: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        startActivityForResult(Intent.createChooser(intent, title), requestCode)
    }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    protected fun createFileForFileProvider():File{
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    @Throws(IOException::class)
    protected fun takePicture(requestCode:Int,authority:String) :String{
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFileForFileProvider()

        val uri: Uri = FileProvider.getUriForFile(
            this,
            authority,
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, requestCode)
        return file.absolutePath
    }

    protected fun writeFileContent(uri: Uri): String {
        var filePath = ""
        contentResolver?.openInputStream(uri)?.use { selectedFileInputStream ->
            val certCacheDir =
                File(getExternalFilesDir(null),"Cache")
            var isCertCacheDirExists = certCacheDir.exists()
            if (!isCertCacheDirExists) {
                isCertCacheDirExists = certCacheDir.mkdirs()
            }

            if (isCertCacheDirExists) {
                filePath = "${certCacheDir.absolutePath}/${getFileDisplayName(uri)}"
                FileOutputStream(filePath).use { selectedFileOutPutStream ->
                    val buffer = ByteArray(1024)
                    while (true) {
                        val length = selectedFileInputStream.read(buffer)
                        if (length <= 0) {
                            break
                        }
                        selectedFileOutPutStream.write(buffer, 0, length)
                    }
                }
            }
        }
        return filePath
    }

    protected fun getFileDisplayName(uri: Uri): String {
        var displayName = ""
        contentResolver
            .query(uri, null, null, null, null, null).use {
                if (it != null && it.moveToFirst()) {
                    val index: Int = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    displayName = it.getString(index)
                }
            }
        return displayName
    }

    protected fun openDocument(requestCode:Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            flags =
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            type = "*/*"
        }
        startActivityForResult(intent, requestCode)
    }

    protected fun getFileSize(uri: Uri): String {
        var size = ""
        contentResolver?.query(uri, null, null, null, null, null).use {
            if (it != null && it.moveToFirst()) {
                val index: Int = it.getColumnIndex(OpenableColumns.SIZE)
                size = it.getString(index)
            }
        }
        return size
    }
}