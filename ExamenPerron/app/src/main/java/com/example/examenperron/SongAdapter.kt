package com.example.examenperron

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(private val songList: List<MainActivity.Song>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentSong = songList[position]
        holder.textViewSongTitle.text = currentSong.title
        holder.imageViewAlbumArt.setImageResource(currentSong.albumArtResourceId)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewAlbumArt: ImageView = itemView.findViewById(R.id.imageViewAlbumArt)
        val textViewSongTitle: TextView = itemView.findViewById(R.id.textViewSongTitle)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
