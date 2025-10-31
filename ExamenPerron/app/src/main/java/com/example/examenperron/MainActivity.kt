package com.example.examenperron

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), MusicWidget.OnMusicControlsClickListener, MediaPlayer.OnCompletionListener, SongAdapter.OnItemClickListener {

    private lateinit var musicWidget: MusicWidget
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var recyclerViewSongs: RecyclerView
    private lateinit var songAdapter: SongAdapter

    data class Song(val title: String, val resourceId: Int, val albumArtResourceId: Int)

    private val songList = listOf(
        Song("Tetris 99", R.raw.tetris99, R.drawable.tetris99),
        Song("Tetris Metal Cover", R.raw.tetris_metal_cover, R.mipmap.ic_launcher) // Placeholder
    )
    private var currentSongIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        musicWidget = findViewById(R.id.musicWidget)
        musicWidget.setOnMusicControlsClickListener(this)

        // Setup RecyclerView
        recyclerViewSongs = findViewById(R.id.recyclerViewSongs)
        recyclerViewSongs.layoutManager = LinearLayoutManager(this)
        songAdapter = SongAdapter(songList, this)
        recyclerViewSongs.adapter = songAdapter

        // Initialize MediaPlayer with the first song
        mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex].resourceId)
        mediaPlayer.setVolume(0.7f, 0.7f) // Set initial volume to 70%
        mediaPlayer.setOnCompletionListener(this) // Set completion listener
        updateMusicWidgetInfo()
    }

    private fun updateMusicWidgetInfo() {
        musicWidget.setSongTitle(songList[currentSongIndex].title)
        musicWidget.setAlbumArt(songList[currentSongIndex].albumArtResourceId)
        musicWidget.setIsPlaying(mediaPlayer.isPlaying)
    }

    private fun playCurrentSong() {
        mediaPlayer.stop()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex].resourceId)
        mediaPlayer.setVolume(0.7f, 0.7f) // Re-apply volume after creating new MediaPlayer
        mediaPlayer.setOnCompletionListener(this) // Re-set completion listener
        mediaPlayer.start()
        musicWidget.setIsPlaying(true)
        updateMusicWidgetInfo()
    }

    override fun onPreviousClick() {
        currentSongIndex = (currentSongIndex - 1 + songList.size) % songList.size
        playCurrentSong()
        Toast.makeText(this, "Previous: ${songList[currentSongIndex].title}", Toast.LENGTH_SHORT).show()
    }

    override fun onPlayPauseClick() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            musicWidget.setIsPlaying(false)
            Toast.makeText(this, "Paused: ${songList[currentSongIndex].title}", Toast.LENGTH_SHORT).show()
        } else {
            mediaPlayer.start()
            musicWidget.setIsPlaying(true)
            Toast.makeText(this, "Playing: ${songList[currentSongIndex].title}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStopClick() {
        mediaPlayer.stop()
        mediaPlayer.prepareAsync() // Prepare for next playback
        musicWidget.setIsPlaying(false)
        Toast.makeText(this, "Stopped: ${songList[currentSongIndex].title}", Toast.LENGTH_SHORT).show()
    }

    override fun onNextClick() {
        currentSongIndex = (currentSongIndex + 1) % songList.size
        playCurrentSong()
        Toast.makeText(this, "Next: ${songList[currentSongIndex].title}", Toast.LENGTH_SHORT).show()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        // When current song finishes, play the next one
        currentSongIndex = (currentSongIndex + 1) % songList.size
        playCurrentSong()
    }

    override fun onItemClick(position: Int) {
        currentSongIndex = position
        playCurrentSong()
        Toast.makeText(this, "Selected: ${songList[currentSongIndex].title}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}