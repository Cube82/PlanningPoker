package pl.cube.planning_poker

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import pl.cube.planning_poker.config.configureRouting
import pl.cube.planning_poker.config.configureSerialization
import pl.cube.planning_poker.config.configureSockets
import pl.cube.planning_poker.game.Game

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    embeddedServer(Netty, host = "0.0.0.0", port = port) {
        module()
    }.start(wait = true)
}

@Suppress("unused")
fun Application.module() {
    val game = Game()
    configureSockets()
    configureSerialization()
    configureRouting(game)
}
