package com.example.examenperron

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast

class MusicAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, false, context.getString(R.string.default_song_title), R.mipmap.ic_launcher)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val action = intent.action
        if (action?.startsWith("com.example.examenperron.ACTION") == true && action != MusicService.ACTION_UPDATE_WIDGET) {
            val serviceIntent = Intent(context, MusicService::class.java)
            serviceIntent.action = action
            context.startService(serviceIntent)
        }

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget =
            ComponentName(context.packageName, MusicAppWidgetProvider::class.java.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)

        for (appWidgetId in appWidgetIds) {
            if (action == MusicService.ACTION_UPDATE_WIDGET) {
                val isPlaying = intent.getBooleanExtra("is_playing", false)
                val songTitle = intent.getStringExtra("song_title")
                val albumArtResId = intent.getIntExtra("album_art_res_id", R.mipmap.ic_launcher)
                updateAppWidget(context, appWidgetManager, appWidgetId, isPlaying, songTitle, albumArtResId)
            }
        }

        super.onReceive(context, intent)
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, isPlaying: Boolean = false, songTitle: String? = null, albumArtResId: Int? = null) {
        val views = RemoteViews(context.packageName, R.layout.music_widget)

        // Set up PendingIntents for each button
        views.setOnClickPendingIntent(R.id.widget_button_prev, getPendingIntent(context, MusicService.ACTION_PREVIOUS))
        views.setOnClickPendingIntent(R.id.widget_button_play_pause, getPendingIntent(context, MusicService.ACTION_TOGGLE_PLAY_PAUSE))
        views.setOnClickPendingIntent(R.id.widget_button_stop, getPendingIntent(context, MusicService.ACTION_STOP))
        views.setOnClickPendingIntent(R.id.widget_button_next, getPendingIntent(context, MusicService.ACTION_NEXT))

        // Set play/pause icon
        views.setImageViewResource(R.id.widget_button_play_pause, if (isPlaying) R.drawable.ic_pause_white else R.drawable.ic_play_white)

        // Set song title
        songTitle?.let { views.setTextViewText(R.id.widget_song_title, it) }

        // Set album art
        albumArtResId?.let { views.setImageViewResource(R.id.mw_img, it) }

        // Intent to launch the main activity
        val launchIntent = Intent(context, MainActivity::class.java)
        val pendingLaunchIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widget_root, pendingLaunchIntent)

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getPendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, MusicAppWidgetProvider::class.java)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}
