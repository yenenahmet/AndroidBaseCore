package com.yenen.ahmet.baseexoplayerlibrary.base

import android.content.Context
import android.net.Uri
import android.os.Handler
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

class MusicManager {

    private var oldSongUri: String? = null
    private var player: SimpleExoPlayer? = null
    private var currentPosition: Long = 0
    private var currentWindowIndex: Int = 0
    private var eventListeners = mutableListOf<MusicManagerListener>()
    private val handler = Handler()

    companion object {
        @Volatile
        private var INSTANCE: MusicManager? = null

        fun getInstance(): MusicManager {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = MusicManager()
                INSTANCE = instance
                return instance
            }
        }
    }

    fun setEventListener(listener: MusicManagerListener) {
        this.eventListeners.remove(listener)
        this.eventListeners.add(listener)
    }

    fun unBindListener(listener: MusicManagerListener){
        this.eventListeners.remove(listener)
    }

    // for fragments in onDetach and for activity in onDestory
    fun release() {
        currentPosition = player?.currentPosition ?: 0
        currentWindowIndex = player?.currentWindowIndex ?: 0
        player?.removeListener(playerEventListener)
        player?.stop()
        player?.release()
        player = null
        eventListeners.clear()
        handler.removeCallbacks(run)
    }

    fun play(fileUrl: String,context: Context) {
        if (player == null) {
            initializePlayer(context)
            val mediaSource = buildMediaSource(fileUrl,context)
            player!!.prepare(mediaSource)
        }


        player?.playWhenReady = true

        if (fileUrl == oldSongUri) {
            player?.seekTo(currentWindowIndex, currentPosition)
        } else {
            val mediaSource = buildMediaSource(fileUrl,context)
            player!!.prepare(mediaSource)
            currentWindowIndex = 0
            currentPosition = 0
            player?.seekTo(0, 0)
        }

        oldSongUri = fileUrl
    }

    fun prepare(fileUrl: String, isPlay: Boolean,context: Context) {
        if (player == null)
            initializePlayer(context)

        val mediaSource = buildMediaSource(fileUrl,context)

        player!!.prepare(mediaSource)
        player?.playWhenReady = isPlay
        player?.seekTo(0, 0)
        oldSongUri = fileUrl
    }

    fun isPlaying(): Boolean {
        return player?.isPlaying ?: false
    }

    fun stopPlayer() {
        currentPosition = player?.currentPosition ?: 0
        currentWindowIndex = player?.currentWindowIndex ?: 0
        player?.playWhenReady = false
        player?.playbackState
    }

    fun startPlayer() {
        player?.playWhenReady = true
        player?.playbackState
    }

    fun clearPosition(){
        this.currentPosition = 0
        this.currentWindowIndex = 0
    }

    fun getCurrentPosition():Long{
        return player?.currentPosition?:0L
    }

    fun getCurrentWindowIndex():Int{
        return player?.currentWindowIndex?:0
    }

    fun setSeekToIfReady(progress:Int){
        if(player!=null){
            val progressX = (progress.toFloat()/100).toFloat()
            val duration = (player?.duration ?: 0).toFloat()
            val x =  progressX* duration
            currentPosition = x.toLong()
            player?.playWhenReady = true
            player?.seekTo(getCurrentWindowIndex(), x.toLong())
        }
    }

    private val playerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {
            eventListeners.forEach {
                if (playbackState == Player.STATE_BUFFERING) {
                    it.onLoading()
                } else if (playbackState == Player.STATE_READY) {
                    it.onContinues()
                } else if (playbackState == Player.STATE_ENDED) {
                    it.onFinish()
                }
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            eventListeners.forEach {
                it.onError(error)
            }
        }

    }

    private fun buildMediaSource(fileUrl: String,context: Context): MediaSource {
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

    private fun updateProgress() {
        val duration = player?.duration ?: 0
        val position = player?.currentPosition ?: 0

        val durText = getFormatTime(duration)
        val posText = getFormatTime(position)

        if (duration >= 0 && player?.playbackState != Player.STATE_ENDED) {
            eventListeners.forEach {
                it.onProgress(
                    posText,
                    durText,
                    ((position * 100) / duration).toInt(),
                    position,
                    duration
                )
            }
        }

        handler.postDelayed(run, 100)
    }

    private fun getFormatTime(ms: Long): String {
        val second = (ms / 1000).toFloat()
        val minute = (second / 60).toInt()

        val exSecond = (minute * 60)

        val difSec = (second - exSecond).toInt()
        val difSecText = if (difSec > 9) {
            difSec.toString()
        } else {
            "0${difSec}"
        }

        val difMinuteText = if (minute > 9) {
            minute.toString()
        } else {
            "0${minute}"
        }
        return "${difMinuteText}:${difSecText}"
    }

    private val run = Runnable { updateProgress() }

    private fun initializePlayer(context: Context) {
        val audioAttributes =
            AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()

        player = SimpleExoPlayer.Builder(context).build().apply {
            setHandleAudioBecomingNoisy(true)
            setAudioAttributes(audioAttributes, true)
            addListener(playerEventListener)
        }

        updateProgress()
    }
}