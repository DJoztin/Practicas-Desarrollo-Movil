package com.example.examenperron

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast

class MusicAppWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val ACTION_PREVIOUS = "com.example.examenperron.ACTION_PREVIOUS"
        private const val ACTION_PLAY_PAUSE = "com.example.examenperron.ACTION_PLAY_PAUSE"
        private const val ACTION_STOP = "com.example.examenperron.ACTION_STOP"
        private const val ACTION_NEXT = "com.example.examenperron.ACTION_NEXT"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (context == null || intent == null) return

        when (intent.action) {
            ACTION_PREVIOUS -> {
                Toast.makeText(context, "Widget: Previous Clicked", Toast.LENGTH_SHORT).show()
                // Handle previous track logic here
            }
            ACTION_PLAY_PAUSE -> {
                Toast.makeText(context, "Widget: Play/Pause Clicked", Toast.LENGTH_SHORT).show()
                // Handle play/pause logic here
            }
            ACTION_STOP -> {
                Toast.makeText(context, "Widget: Stop Clicked", Toast.LENGTH_SHORT).show()
                // Handle stop logic here
            }
            ACTION_NEXT -> {
                Toast.makeText(context, "Widget: Next Clicked", Toast.LENGTH_SHORT).show()
                // Handle next track logic here
            }
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.music_widget)

        // Set up PendingIntents for each button
        views.setOnClickPendingIntent(R.id.widget_button_prev, getPendingIntent(context, ACTION_PREVIOUS))
        views.setOnClickPendingIntent(R.id.widget_button_play_pause, getPendingIntent(context, ACTION_PLAY_PAUSE))
        views.setOnClickPendingIntent(R.id.widget_button_stop, getPendingIntent(context, ACTION_STOP))
        views.setOnClickPendingIntent(R.id.widget_button_next, getPendingIntent(context, ACTION_NEXT))

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getPendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, MusicAppWidgetProvider::class.java)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}
