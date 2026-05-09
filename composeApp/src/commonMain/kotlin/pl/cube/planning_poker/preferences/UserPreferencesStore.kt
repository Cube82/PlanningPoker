package pl.cube.planning_poker.preferences

internal expect object UserPreferencesStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
}
