package pl.cube.planning_poker.ui.settings

enum class AppThemeMode {
    System,
    Light,
    Dark,
}

enum class AppLanguage {
    System,
    English,
    Polish,
}

data class AppSettingsState(
    val themeMode: AppThemeMode = AppThemeMode.System,
    val language: AppLanguage = AppLanguage.System,
)
