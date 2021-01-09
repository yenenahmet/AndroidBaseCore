package com.yenen.ahmet.baseexoplayerlibrary.base

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.WindowCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.yenen.ahmet.base_exoplayer.R

class FullScreenExoPlayerActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null
    private var videoView: PlayerView? = null
    private var fileUrl: String? = ""
    private var currentPosition: Long? = 0
    private var currentWindowIndex: Int? = 0
    private var playWhenReady = true

    companion object {
        const val URL = "URL"
        const val PLAY_WHEN_READY = "PLAY_WHEN_READY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_exo_player)
        videoView = findViewById(R.id.playerView)
        val close = findViewById<AppCompatImageView>(R.id.imgClose)
        close.setOnClickListener {
            finish()
        }
        fileUrl = intent.getStringExtra(BaseFullScreenCacheExoPlayerActivity.URL)
        playWhenReady = intent.getBooleanExtra(BaseFullScreenCacheExoPlayerActivity.PLAY_WHEN_READY, true)

    }


    private fun buildMediaSource(fileUrl: String): MediaSource {
        val uri = Uri.parse(fileUrl)
        val defaultDataSourceFactory = if (fileUrl.startsWith("http")) {
            DefaultHttpDataSourceFactory("base_exo")
        } else {
            DefaultDataSourceFactory(this, "base_exo")
        }
        return ProgressiveMediaSource.Factory(
                defaultDataSourceFactory,
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
        WindowCompat.setDecorFitsSystemWindows(window,false)
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }
}
