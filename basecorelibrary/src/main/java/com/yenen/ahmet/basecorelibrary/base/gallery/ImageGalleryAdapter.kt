package com.yenen.ahmet.basecorelibrary.base.gallery

import com.bumptech.glide.Glide
import com.yenen.ahmet.basecorelibrary.R
import com.yenen.ahmet.basecorelibrary.base.adapter.BaseViewBindingPagerAdapter
import com.yenen.ahmet.basecorelibrary.databinding.ImageGalleryPagerItemBinding

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
    }


}