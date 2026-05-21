package pl.cube.planning_poker.config

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticFiles
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import pl.cube.planning_poker.game.Game
import pl.cube.planning_poker.tableSocket
import java.io.File

fun Application.configureRouting(game: Game) {
    routing {
        tableSocket(game)
        frontendStaticContent()
    }
}

private fun Route.frontendStaticContent() {
    val frontendDir = File(System.getenv("FRONTEND_STATIC_DIR") ?: "public")
    val indexFile = frontendDir.resolve("index.html")

    if (!frontendDir.exists() || !indexFile.exists()) {
        return
    }

    staticFiles("/", frontendDir) {
        default("index.html")
    }

    get("{...}") {
        call.respondFile(indexFile)
    }
}
