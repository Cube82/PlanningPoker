package pl.cube.planning_poker

import io.ktor.server.application.Application
import pl.cube.planning_poker.config.configureRouting
import pl.cube.planning_poker.config.configureSerialization
import pl.cube.planning_poker.config.configureSockets
import pl.cube.planning_poker.game.Game

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
    val game = Game()
    configureSockets()
    configureSerialization()
    configureRouting(game)
}