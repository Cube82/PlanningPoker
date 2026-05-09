package pl.cube.planning_poker.preferences

import kotlinx.browser.window

internal actual object UserPreferencesStore {
    actual fun getString(key: String): String? {
        return window.localStorage.getItem(key)
    }

    actual fun putString(key: String, value: String) {
        window.localStorage.setItem(key, value)
    }

    actual fun remove(key: String) {
        window.localStorage.removeItem(key)
    }
}
