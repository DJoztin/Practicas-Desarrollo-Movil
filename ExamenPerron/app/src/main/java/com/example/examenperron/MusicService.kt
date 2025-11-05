package com.example.examenperron

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicService : Service(), MediaPlayer.OnCompletionListener {

    private lateinit var mediaPlayer: MediaPlayer
    var currentSongIndex = 0
        private set

    data class Song(val title: String, val resourceId: Int, val albumArtResourceId: Int)

    val songList = listOf(
        Song("Gourmet Race", R.raw.gourmet_race, R.drawable.ic_kirby_song),
        Song("Mario Water", R.raw.mario_water, R.drawable.ic_mm_icon),
        Song("Minecraft Main Theme", R.raw.minecraft_main, R.drawable.ic_mc_song),
        Song("Tetris Metal Cover", R.raw.tetris_metal_cover, R.drawable.ic_tetris_song),
        Song("Wii Sports", R.raw.wii_sports, R.drawable.ic_wii_song),
        Song("You Get What You Give", R.raw.you_get_what_you_give, R.drawable.ic_gg_song)
    )

    companion object {
        const val ACTION_PLAY = "com.example.examenperron.ACTION_PLAY"
        const val ACTION_PAUSE = "com.example.examenperron.ACTION_PAUSE"
        const val ACTION_STOP = "com.example.examenperron.ACTION_STOP"
        const val ACTION_NEXT = "com.example.examenperron.ACTION_NEXT"
        const val ACTION_PREVIOUS = "com.example.examenperron.ACTION_PREVIOUS"
        const val ACTION_PLAY_SONG = "com.example.examenperron.ACTION_PLAY_SONG"
        const val ACTION_SONG_CHANGED = "com.example.examenperron.SONG_CHANGED"
        const val ACTION_TOGGLE_PLAY_PAUSE = "com.example.examenperron.ACTION_TOGGLE_PLAY_PAUSE"
        const val ACTION_UPDATE_WIDGET = "com.example.examenperron.ACTION_UPDATE_WIDGET"
    }

    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex].resourceId)
        mediaPlayer.setOnCompletionListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                mediaPlayer.start()
                broadcastUpdateWidget()
            }
            ACTION_PAUSE -> {
                mediaPlayer.pause()
                broadcastUpdateWidget()
            }
            ACTION_TOGGLE_PLAY_PAUSE -> {
                togglePlayPause()
            }
            ACTION_STOP -> {
                stopMusic()
            }
            ACTION_NEXT -> {
                playNextSong()
            }
            ACTION_PREVIOUS -> {
                playPreviousSong()
            }
            ACTION_PLAY_SONG -> {
                currentSongIndex = intent.getIntExtra("song_index", 0)
                playCurrentSong()
            }
        }
        return START_STICKY
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun togglePlayPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
        broadcastUpdateWidget()
    }

    fun stopMusic() {
        mediaPlayer.stop()
        mediaPlayer.prepareAsync()
        broadcastUpdateWidget()
    }

    fun playSongAt(position: Int) {
        currentSongIndex = position
        playCurrentSong()
    }

    fun playNextSong() {
        currentSongIndex = (currentSongIndex + 1) % songList.size
        playCurrentSong()
    }

    fun playPreviousSong() {
        currentSongIndex = (currentSongIndex - 1 + songList.size) % songList.size
        playCurrentSong()
    }

    fun playCurrentSong() {
        mediaPlayer.stop()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex].resourceId)
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.start()
        broadcastUpdateWidget()
    }

    private fun broadcastUpdateWidget() {
        val intent = Intent(this, MusicAppWidgetProvider::class.java)
        intent.action = ACTION_UPDATE_WIDGET
        intent.putExtra("is_playing", mediaPlayer.isPlaying)
        intent.putExtra("song_title", songList[currentSongIndex].title)
        intent.putExtra("album_art_res_id", songList[currentSongIndex].albumArtResourceId)
        sendBroadcast(intent)
    }


    override fun onCompletion(mp: MediaPlayer?) {
        playNextSong()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}