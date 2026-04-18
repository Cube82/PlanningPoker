package pl.cube.planning_poker.net

import kotlinx.browser.window

internal actual object BackendConfig {
    actual val webSocketUrl: String
        get() {
            val protocol = if (window.location.protocol == "https:") "wss" else "ws"
            val host = window.location.hostname.ifBlank { "127.0.0.1" }
            return "$protocol://$host:8080/table"
        }
}
