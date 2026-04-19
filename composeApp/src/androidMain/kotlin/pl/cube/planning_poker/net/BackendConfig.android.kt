package pl.cube.planning_poker.net

internal actual object BackendConfig {
    // With `adb reverse tcp:8080 tcp:8080`, Android can reach local backend via loopback.
    actual val webSocketUrl: String = "ws://127.0.0.1:8080/table"
}
