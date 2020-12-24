package com.yenen.ahmet.baseexoplayerlibrary.base

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import com.yenen.ahmet.base_exoplayer.R

abstract class BaseFullScreenCacheExoPlayerActivity : AppCompatActivity() {

    companion object{
        const val USED_CACHE_SIZE = "CACHE"
        const val URL = "URL"
        const val PLAY_WHEN_READY = "PLAY_WHEN_READY"
    }

    private var player: SimpleExoPlayer? = null
    private var videoView: PlayerView? = null
    private var fileUrl: String? = ""
    private var currentPosition: Long? = 0
    private var currentWindowIndex: Int? = 0
    private var simpleCache: SimpleCache?=null
    private var playWhenReady = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_cache_exo_player)
        videoView = findViewById(R.id.playerView)

        val cacheBundle:Long? = intent.getLongExtra(USED_CACHE_SIZE,0)
        fileUrl = intent.getStringExtra(URL)
        playWhenReady = intent.getBooleanExtra(PLAY_WHEN_READY,true)

        val exoDatabaseProvider = ExoDatabaseProvider(this)

       if(cacheBundle!=null && cacheBundle>0){
            val usedCache = LeastRecentlyUsedCacheEvictor(cacheBundle)
            simpleCache =SimpleCache(cacheDir,usedCache,exoDatabaseProvider)
        }


    }


    private fun buildMediaSource(fileUrl: String): MediaSource {

        val uri = Uri.parse(fileUrl)
        val cacheDataSourceFactory = if (fileUrl.startsWith("http")) {
            CacheDataSourceFactory(
                    simpleCache,
                    DefaultHttpDataSourceFactory(Util.getUserAgent(this,getAppName())),
                    CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
            )
        } else {
            CacheDataSourceFactory(
                    simpleCache,
                    DefaultDataSourceFactory(this,  Util.getUserAgent(this,getAppName())),
                    CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
            )
        }
        return ProgressiveMediaSource.Factory(
                cacheDataSourceFactory,
                DefaultExtractorsFactory()
        ).createMediaSource(uri)

    }

    private fun playVideo() {
        fileUrl?.let {
            val mediaSource = buildMediaSource(it)
            player?.seekTo(currentWindowIndex!!, currentPosition!!)
            player?.prepare(mediaSource, false, false)
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        currentPosition = player?.currentPosition
        currentWindowIndex = player?.currentWindowIndex
        player?.playWhenReady = false
        player?.stop()
        player?.release()
        player = null
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this).build()
        videoView?.player = player

        player?.playWhenReady = playWhenReady
        player?.repeatMode = Player.REPEAT_MODE_OFF
        playVideo()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    abstract protected fun getAppName():String

}
