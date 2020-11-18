package com.yenen.ahmet.basecorelibrary.base.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.ViewPager
import com.yenen.ahmet.basecorelibrary.R
import com.yenen.ahmet.basecorelibrary.base.extension.screenBarClear
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.lang.Exception

class ImageGalleryActivity : AppCompatActivity() {

    private val imgAdapter = ImageGalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_gallery)
        val pager = findViewById<ViewPager>(R.id.viewPager)

        intent.getStringArrayExtra("IMAGES")?.let {
            imgAdapter.setItems(it.toList())
        }
        val position = intent.getIntExtra("POSITION",0)

        pager.adapter = imgAdapter
        pager.setPageTransformer(true, ZoomOutPageTransformer())

        try{
            pager.setCurrentItem(position,true)
        }catch (ex:Exception){

        }

        val back = findViewById<AppCompatImageView>(R.id.imgClose)
        back.setOnClickListener {
            finish()
        }

        val indicator = findViewById<ScrollingPagerIndicator>(R.id.indicator)
        indicator.attachToPager(pager)

        screenBarClear()
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if(hasFocus){
            screenBarClear()
        }
    }


}
