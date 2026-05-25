package pl.cube.planning_poker.config

import io.ktor.server.application.Application
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
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

    get("{path...}") {
        val path = call.parameters.getAll("path").orEmpty()
        if (path.isEmpty()) {
            call.respondFile(indexFile)
            return@get
        }

        val requestedFile = frontendDir.resolve(path.joinToString(File.separator)).canonicalFile
        val canonicalFrontendDir = frontendDir.canonicalFile

        if (!requestedFile.path.startsWith(canonicalFrontendDir.path + File.separator)) {
            call.respond(HttpStatusCode.Forbidden)
            return@get
        }

        if (requestedFile.isFile) {
            call.respondFile(requestedFile)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
