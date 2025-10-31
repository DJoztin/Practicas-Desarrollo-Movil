package com.example.examenperron

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.examenperron.databinding.MusicWidgetBinding

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

    private var binding: MusicWidgetBinding =
        MusicWidgetBinding.inflate(LayoutInflater.from(context), this)

    init {
        binding.widgetButtonPrev.setOnClickListener { listener?.onPreviousClick() }
        binding.widgetButtonPlayPause.setOnClickListener {
            isPlaying = !isPlaying
            togglePlayPause(isPlaying)
            listener?.onPlayPauseClick()
        }
        binding.widgetButtonStop.setOnClickListener { listener?.onStopClick() }
        binding.widgetButtonNext.setOnClickListener { listener?.onNextClick() }
    }

    fun setOnMusicControlsClickListener(l: OnMusicControlsClickListener?) {
        listener = l
    }

    fun setSongTitle(title: String) {
        binding.widgetSongTitle.text = title
    }

    fun setAlbumArt(resId: Int) {
        binding.mwImg.setImageResource(resId)
    }

    fun togglePlayPause(playing: Boolean) {
        isPlaying = playing
        val iconRes = if (isPlaying) R.drawable.ic_pause_white else R.drawable.ic_play_white
        binding.widgetButtonPlayPause.setImageDrawable(ContextCompat.getDrawable(context, iconRes))
    }

    fun setIsPlaying(playing: Boolean) {
        isPlaying = playing
        togglePlayPause(isPlaying)
    }
}