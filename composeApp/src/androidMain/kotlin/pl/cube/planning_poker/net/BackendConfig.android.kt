package pl.cube.planning_poker.net

internal actual object BackendConfig {
    // 10.0.2.2 maps the host machine loopback to the Android emulator.
    actual val webSocketUrl: String = "ws://10.0.2.2:8080/table"
}
