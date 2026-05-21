package pl.cube.planning_poker.net

import kotlinx.browser.window

internal actual object BackendConfig {
    actual val webSocketUrl: String
        get() {
            val protocol = if (window.location.protocol == "https:") "wss" else "ws"
            val host = window.location.hostname.ifBlank { "127.0.0.1" }
            val isLocalHost = host == "localhost" || host == "127.0.0.1" || host == "::1"
            val backendHost = if (isLocalHost && window.location.port != "8080") {
                "$host:8080"
            } else {
                window.location.host.ifBlank { "$host:8080" }
            }

            return "$protocol://$backendHost/table"
        }
}
