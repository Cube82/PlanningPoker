package pl.cube.planning_poker.preferences

import android.content.Context
import android.content.SharedPreferences

private const val PREFERENCES_NAME = "planning_poker_preferences"

fun initializeUserPreferences(context: Context) {
    UserPreferencesStore.initialize(context.applicationContext)
}

internal actual object UserPreferencesStore {
    private var preferences: SharedPreferences? = null

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    actual fun getString(key: String): String? {
        return preferences?.getString(key, null)
    }

    actual fun putString(key: String, value: String) {
        preferences?.edit()?.putString(key, value)?.apply()
    }

    actual fun remove(key: String) {
        preferences?.edit()?.remove(key)?.apply()
    }
}
