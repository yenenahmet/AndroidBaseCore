package com.yenen.ahmet.baseexoplayerlibrary.base

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import java.io.File

class MusicManager(
    private val context: Context,
    private val controlView: PlayerControlView,
    private val appName: String,
    private val isHandleWeakLock: Boolean,
    private val simpleCache: SimpleCache
) {

    private var oldSongUri: String? = null
    private var player: SimpleExoPlayer? = null
    private var currentPosition: Long? = 0
    private var currentWindowIndex: Int? = 0
    private var eventListener: MusicManagerListener? = null


    fun setEventListener(listener: MusicManagerListener) {
        this.eventListener = null
        this.eventListener = listener
    }

    private fun initializePlayer() {
        val audioAttributes =
            AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()

        player = SimpleExoPlayer.Builder(context).build().apply {
            if (isHandleWeakLock) {
                setHandleWakeLock(true)
            }
            setHandleAudioBecomingNoisy(true)
            setAudioAttributes(audioAttributes, true)
            addListener(playerEventListener)
        }
        controlView.player = player
    }

    private val playerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {
            eventListener?.onEventListener(playbackState)
        }
    }

    fun release() {
        currentPosition = player?.currentPosition
        currentWindowIndex = player?.currentWindowIndex
        player?.removeListener(playerEventListener)
        player?.stop()
        player?.release()
        eventListener = null
    }

    fun play(fileUrl: String) {

        if (player == null)
            initializePlayer()


        val mediaSource = buildMediaSource(fileUrl)

        if (fileUrl == oldSongUri) {
            player?.seekTo(currentWindowIndex!!, currentPosition!!)
        } else {
            currentWindowIndex = 0
            currentPosition = 0
            player?.seekTo(0, 0)
        }

        player!!.prepare(mediaSource)
        player?.playWhenReady = true


        oldSongUri = fileUrl
    }


    private fun buildMediaSource(fileUrl: String): MediaSource {
        val uri = Uri.parse(fileUrl)
        val cacheDataSourceFactory = if (fileUrl.startsWith("http")) {
            CacheDataSourceFactory(
                simpleCache,
                DefaultHttpDataSourceFactory(Util.getUserAgent(context, appName)),
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
            )
        } else {
            CacheDataSourceFactory(
                simpleCache,
                DefaultDataSourceFactory(context, Util.getUserAgent(context, appName)),
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
            )
        }
        return ProgressiveMediaSource.Factory(
            cacheDataSourceFactory,
            DefaultExtractorsFactory()
        ).createMediaSource(uri)
    }

    fun isPlaying():Boolean{
        return player?.isPlaying?:false
    }

    fun stopPlayer(){
        player?.playWhenReady = false
        player?.playbackState
    }

    fun startPlayer(){
        player?.playWhenReady = true
        player?.playbackState
    }
}