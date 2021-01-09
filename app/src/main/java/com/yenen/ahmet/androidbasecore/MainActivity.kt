package com.yenen.ahmet.androidbasecore

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import com.yenen.ahmet.androidbasecore.databinding.ActivityMainBinding
import com.yenen.ahmet.basecorelibrary.base.extension.showToast
import com.yenen.ahmet.basecorelibrary.base.ui.BaseActivity


class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>(MainViewModel::class.java,R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionsForRuntime(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
    }

    override fun initViewModel(viewModel: MainViewModel) {
        binding.viewModel = viewModel
    }


    override fun onRequestPermissionResultForRuntime(isGranted: Boolean) {
        if(isGranted){
            showToast("Kabul edildi")
        }else{
            showToast("Kabul edilmedi")
        }
    }
}
