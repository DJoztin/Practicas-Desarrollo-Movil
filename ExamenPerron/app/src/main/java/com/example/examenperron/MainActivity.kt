package com.example.examenperron

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), MusicWidget.OnMusicControlsClickListener, SongAdapter.OnItemClickListener {

    private lateinit var musicWidget: MusicWidget
    private lateinit var recyclerViewSongs: RecyclerView
    private lateinit var songAdapter: SongAdapter

    private var musicService: MusicService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
            // Initialize RecyclerView and Adapter here
            recyclerViewSongs = findViewById(R.id.recyclerViewSongs)
            recyclerViewSongs.layoutManager = LinearLayoutManager(this@MainActivity)
            songAdapter = SongAdapter(musicService!!.songList, this@MainActivity) {
                val retriever = MediaMetadataRetriever()
                val uri = Uri.parse("android.resource://${packageName}/${it.resourceId}")
                retriever.setDataSource(this@MainActivity, uri)
                val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val duration = durationStr?.toLongOrNull() ?: 0
                String.format("%d:%02d", (duration / 1000) / 60, (duration / 1000) % 60)
            }
            recyclerViewSongs.adapter = songAdapter
            updateMusicWidgetInfo()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        musicWidget = findViewById(R.id.musicWidget)
        musicWidget.setOnMusicControlsClickListener(this)

        val serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun updateMusicWidgetInfo() {
        if (isBound) {
            musicService?.let {
                musicWidget.setSongTitle(it.songList[it.currentSongIndex].title)
                musicWidget.setAlbumArt(it.songList[it.currentSongIndex].albumArtResourceId)
                musicWidget.setIsPlaying(it.isPlaying())
            }
        }
    }

    override fun onPreviousClick() {
        musicService?.playPreviousSong()
        updateMusicWidgetInfo()
    }

    override fun onPlayPauseClick() {
        musicService?.togglePlayPause()
        updateMusicWidgetInfo()
    }

    override fun onStopClick() {
        musicService?.stopMusic()
        updateMusicWidgetInfo()
    }

    override fun onNextClick() {
        musicService?.playNextSong()
        updateMusicWidgetInfo()
    }

    override fun onItemClick(position: Int) {
        musicService?.playSongAt(position)
        updateMusicWidgetInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }
}