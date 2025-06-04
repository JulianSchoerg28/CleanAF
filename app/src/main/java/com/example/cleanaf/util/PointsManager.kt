package com.example.cleanaf.util

import android.content.Context

object PointsManager {
    private const val PREFS_NAME = "points_prefs"
    private const val KEY_TOTAL_POINTS = "total_points"

    fun addPoints(context: Context, points: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentPoints = prefs.getInt(KEY_TOTAL_POINTS, 0)
        prefs.edit().putInt(KEY_TOTAL_POINTS, currentPoints + points).apply()
    }

    fun getPoints(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_TOTAL_POINTS, 0)
    }

}
