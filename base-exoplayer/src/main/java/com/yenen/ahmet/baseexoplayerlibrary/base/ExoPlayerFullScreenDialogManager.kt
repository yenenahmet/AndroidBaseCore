package com.yenen.ahmet.baseexoplayerlibrary.base

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.yenen.ahmet.base_exoplayer.R


class ExoPlayerFullScreenDialogManager(
    private val context: Context,
    @DrawableRes private val imgFullScreenExpand: Int,
    @DrawableRes private val imgFullScreenSkrink: Int,
    private val playerView: PlayerView,
    private val frameExoPlayerView: FrameLayout,
    private val fullScreenIcoId:Int,
    private val fullScreenButtonIn:Int
) {


    private var mExoPlayerFullscreen = false
    private var mFullScreenDialog: Dialog? = null
    private var mFullScreenIcon: ImageView? = null
    private var currentWindowIndex = 0
    private var currentPosition = 0L
    private var player: SimpleExoPlayer? = null
    private var listener:ExoPlayerFullScreenDialogListener?=null


    init {
        initFullscreenDialog()
    }


    fun setListener(listener: ExoPlayerFullScreenDialogListener){
        this.listener = null
        this.listener = listener
    }

    fun unBind(){
        this.listener = null
    }

    private fun initFullscreenDialog() {
        mFullScreenDialog =
            object : Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                override fun onBackPressed() {
                    if (mExoPlayerFullscreen)
                        closeFullscreenDialog()
                    super.onBackPressed()
                }
            }
    }

    private fun initFullScreenButton() {
        val controlView = playerView.findViewById<PlayerControlView>(R.id.exo_controller)
        mFullScreenIcon = controlView?.findViewById(fullScreenIcoId)
        val mFullScreenButton = controlView?.findViewById<FrameLayout>(fullScreenButtonIn)
        mFullScreenButton?.setOnClickListener {
            if (!mExoPlayerFullscreen)
                openFullscreenDialog()
            else
                closeFullscreenDialog()
        }
    }

    private fun openFullscreenDialog() {
        (playerView.parent as ViewGroup).removeView(playerView)
        mFullScreenDialog?.addContentView(
            playerView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        mFullScreenIcon?.setImageDrawable(
            ContextCompat.getDrawable(
                context, imgFullScreenSkrink
            )
        )
        mExoPlayerFullscreen = true
        mFullScreenDialog?.show()

    }

    private fun closeFullscreenDialog() {
        (playerView.parent as ViewGroup).removeView(playerView)
        frameExoPlayerView.addView(playerView)
        mExoPlayerFullscreen = false
        mFullScreenDialog?.dismiss()
        mFullScreenIcon?.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                imgFullScreenExpand
            )
        )
    }


    private fun buildMediaSource(fileUrl: String): MediaSource {
        val uri = Uri.parse(fileUrl)
        val defaultDataSourceFactory = if (fileUrl.startsWith("http")) {
            DefaultHttpDataSourceFactory("base_exo")
        } else {
            DefaultDataSourceFactory(context, "base_exo")
        }
        return ProgressiveMediaSource.Factory(
            defaultDataSourceFactory,
            DefaultExtractorsFactory()
        ).createMediaSource(uri)

    }

    fun playVideo(url:String,playWhenReady:Boolean,repeatMode:Int) {
        if(player == null){
            initializePlayer(playWhenReady,repeatMode)
        }
        val mediaSource = buildMediaSource(url)
        player?.seekTo(currentWindowIndex, currentPosition)
        player?.prepare(mediaSource, false, false)
    }


    fun releasePlayer() {
        currentPosition = player?.currentPosition ?: 0
        currentWindowIndex = player?.currentWindowIndex ?: 0
        player?.playWhenReady = false
        player?.removeListener(playerEventListener)
        player?.stop()
        player?.release()
        player = null
    }

    private fun initializePlayer(playWhenReady:Boolean,repeatMode:Int) {
        player = SimpleExoPlayer.Builder(context).build().apply {
            addListener(playerEventListener)
        }
        playerView.player = player

        player?.playWhenReady = playWhenReady
        player?.repeatMode = repeatMode
    }


    private val playerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {

                if (playbackState == Player.STATE_BUFFERING) {
                    listener?.onLoading()
                } else if (playbackState == Player.STATE_READY) {
                    listener?.onContinues()
                } else if (playbackState == Player.STATE_ENDED) {
                    listener?.onFinish()
                }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            listener?.onError(error)
        }

    }





}