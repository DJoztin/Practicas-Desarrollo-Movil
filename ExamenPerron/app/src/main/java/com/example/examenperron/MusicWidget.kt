package com.example.examenperron

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class MusicWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    interface OnMusicControlsClickListener {
        fun onPreviousClick()
        fun onPlayPauseClick()
        fun onStopClick()
        fun onNextClick()
    }

    private var listener: OnMusicControlsClickListener? = null
    private var isPlaying: Boolean = false

    // Define views
    private val albumArt: ImageView
    private val songTitle: TextView
    private val prevButton: ImageButton
    private val playPauseButton: ImageButton
    private val stopButton: ImageButton
    private val nextButton: ImageButton

    init {
        // Inflate the new layout into this custom view
        LayoutInflater.from(context).inflate(R.layout.view_music_widget, this, true)

        // Find views by their IDs
        albumArt = findViewById(R.id.mw_img)
        songTitle = findViewById(R.id.widget_song_title)
        prevButton = findViewById(R.id.widget_button_prev)
        playPauseButton = findViewById(R.id.widget_button_play_pause)
        stopButton = findViewById(R.id.widget_button_stop)
        nextButton = findViewById(R.id.widget_button_next)

        // Set listeners
        prevButton.setOnClickListener { listener?.onPreviousClick() }
        playPauseButton.setOnClickListener {
            isPlaying = !isPlaying
            togglePlayPause(isPlaying)
            listener?.onPlayPauseClick()
        }
        stopButton.setOnClickListener { listener?.onStopClick() }
        nextButton.setOnClickListener { listener?.onNextClick() }
    }

    fun setOnMusicControlsClickListener(l: OnMusicControlsClickListener?) {
        listener = l
    }

    fun setSongTitle(title: String) {
        songTitle.text = title
    }

    fun setAlbumArt(resId: Int) {
        albumArt.setImageResource(resId)
    }

    fun togglePlayPause(playing: Boolean) {
        isPlaying = playing
        val iconRes = if (isPlaying) R.drawable.ic_pause_white else R.drawable.ic_play_white
        playPauseButton.setImageDrawable(ContextCompat.getDrawable(context, iconRes))
    }

    fun setIsPlaying(playing: Boolean) {
        isPlaying = playing
        togglePlayPause(isPlaying)
    }
}