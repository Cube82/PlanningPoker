package pl.cube.planning_poker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform