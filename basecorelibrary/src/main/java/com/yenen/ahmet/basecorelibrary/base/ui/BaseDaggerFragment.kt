package com.yenen.ahmet.basecorelibrary.base.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yenen.ahmet.basecorelibrary.base.di.factory.AppViewModelFactory
import com.yenen.ahmet.basecorelibrary.base.viewmodel.BaseViewModel
import dagger.android.support.DaggerFragment
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

abstract class BaseDaggerFragment<VM : BaseViewModel, DB : ViewDataBinding>
    (private val viewModelClass: Class<VM>, @LayoutRes private val layoutRes: Int) :
    DaggerFragment() {

    @Inject
    lateinit var providerFactory: AppViewModelFactory

    protected lateinit var binding: DB

    protected val viewModel by lazy {
        ViewModelProvider(this, providerFactory).get(viewModelClass)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,layoutRes, container, false)
        viewModel.setViewDataBinding(binding)
        initViewModel(viewModel)

        onBindingCreate(binding)

        arguments?.let {
            onBundle(it)
        }
        return binding.root
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


    protected fun showToast(text: String) {
        Toast.makeText(activity!!, text, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        onBindingClear(binding)
    }

    protected fun hideKeyboard(){
        activity?.currentFocus?.let {
            val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    protected fun startActivity(sClass: Class<*>) {
        val intent = Intent(activity, sClass)
        startActivity(intent)
    }

    protected fun startActivity(sClass: Class<*>,bundle: Bundle) {
        val intent = Intent(activity, sClass)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    protected fun reStartApp(sClass: Class<*>){
        val intent = Intent(activity!!.applicationContext, sClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity!!.finish()
        System.exit(0)
    }

    protected open fun onBundle(bundle: Bundle){

    }

    protected fun openFile(uri: Uri, fileType:String, title:String):Boolean{
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
                data = Uri.fromParts("package", activity?.packageName, null)
            }
            activity?.startActivity(intent)
            true
        } catch (ex: Exception) {
            false
        }
    }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    protected fun createFileForFileProvider():File{
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
            activity!!,
            authority,
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, requestCode)
        return file.absolutePath
    }
}
