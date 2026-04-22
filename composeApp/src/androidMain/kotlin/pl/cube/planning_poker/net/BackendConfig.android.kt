package pl.cube.planning_poker.net

internal actual object BackendConfig {
    // Local backend is exposed to Android via adb reverse during local debug install.
    actual val webSocketUrl: String = "ws://127.0.0.1:8080/table"
}
