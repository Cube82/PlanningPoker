package pl.cube.planning_poker.preferences

import pl.cube.planning_poker.ui.settings.AppThemeMode

internal class AppSettingsRepository {
    fun getThemeMode(): AppThemeMode {
        return UserPreferencesStore.getString(KEY_THEME_MODE)
            ?.let { value -> runCatching { AppThemeMode.valueOf(value) }.getOrNull() }
            ?: AppThemeMode.System
    }

    fun setThemeMode(themeMode: AppThemeMode) {
        UserPreferencesStore.putString(KEY_THEME_MODE, themeMode.name)
    }

    fun getLastPlayerName(): String {
        return UserPreferencesStore.getString(KEY_LAST_PLAYER_NAME).orEmpty()
    }

    fun setLastPlayerName(playerName: String) {
        val trimmedPlayerName = playerName.trim()
        if (trimmedPlayerName.isBlank()) {
            UserPreferencesStore.remove(KEY_LAST_PLAYER_NAME)
        } else {
            UserPreferencesStore.putString(KEY_LAST_PLAYER_NAME, trimmedPlayerName)
        }
    }

    private companion object {
        const val KEY_THEME_MODE = "themeMode"
        const val KEY_LAST_PLAYER_NAME = "lastPlayerName"
    }
}
