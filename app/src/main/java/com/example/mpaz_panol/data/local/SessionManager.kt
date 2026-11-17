package com.example.mpaz_panol.data.local

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("mpaz_session", Context.MODE_PRIVATE)

    fun saveSession(userId: Int) {
        prefs.edit().putInt("LOGGED_USER_ID", userId).apply()
    }

    fun getLoggedUserId(): Int {
        return prefs.getInt("LOGGED_USER_ID", -1)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}