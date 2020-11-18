package com.yenen.ahmet.basecorelibrary.base.gallery

import android.app.Activity
import com.bumptech.glide.Glide
import com.yenen.ahmet.basecorelibrary.R
import com.yenen.ahmet.basecorelibrary.base.adapter.BaseViewBindingPagerAdapter
import com.yenen.ahmet.basecorelibrary.databinding.ImageGalleryPagerItemBinding
import com.ablanco.zoomy.Zoomy


class ImageGalleryAdapter : BaseViewBindingPagerAdapter<ImageGalleryPagerItemBinding, String>(
    R.layout.image_gallery_pager_item,
    mutableListOf()
) {
    override fun setBindingModel(
        binding: ImageGalleryPagerItemBinding,
        item: String,
        position: Int
    ) {
        Glide.with(binding.root.context).load(item).into(binding.img)

        val builder = Zoomy.Builder(binding.root.context as Activity).target(binding.img)
        builder.register()
    }


}