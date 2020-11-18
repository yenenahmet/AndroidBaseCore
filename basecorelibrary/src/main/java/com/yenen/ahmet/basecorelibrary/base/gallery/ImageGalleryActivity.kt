package com.yenen.ahmet.basecorelibrary.base.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.yenen.ahmet.basecorelibrary.R

class ImageGalleryActivity : AppCompatActivity() {

    private val imgAdapter = ImageGalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_gallery)
        val pager = findViewById<ViewPager>(R.id.viewPager)

        intent.getStringArrayExtra("IMAGES")?.let {
            imgAdapter.setItems(it.toList())
        }

        pager.adapter = imgAdapter
        pager.setPageTransformer(true, ZoomOutPageTransformer())

    }


}
