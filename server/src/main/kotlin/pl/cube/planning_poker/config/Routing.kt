package pl.cube.planning_poker.config

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import pl.cube.planning_poker.game.Game
import pl.cube.planning_poker.tableSocket

fun Application.configureRouting(game: Game) {
    routing {
        tableSocket(game)
    }
}