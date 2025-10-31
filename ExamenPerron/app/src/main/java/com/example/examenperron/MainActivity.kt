package com.example.examenperron

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), MusicWidget.OnMusicControlsClickListener {

    private lateinit var musicWidget: MusicWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        musicWidget = findViewById(R.id.musicWidget)
        musicWidget.setOnMusicControlsClickListener(this)

        // Example usage of MusicWidget's public APIs
        musicWidget.setSongTitle("My Awesome Song")
        musicWidget.setAlbumArt(R.mipmap.ic_launcher)
        musicWidget.setIsPlaying(false)
    }

    override fun onPreviousClick() {
        Toast.makeText(this, "Previous Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onPlayPauseClick() {
        Toast.makeText(this, "Play/Pause Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onStopClick() {
        Toast.makeText(this, "Stop Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onNextClick() {
        Toast.makeText(this, "Next Clicked", Toast.LENGTH_SHORT).show()
    }
}