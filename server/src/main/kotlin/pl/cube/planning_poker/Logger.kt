package pl.cube.planning_poker

import io.ktor.util.logging.KtorSimpleLogger

internal object Logger {
    private val _logger = KtorSimpleLogger("PP")

    fun e(exception: Exception) {
        _logger.error(exception.message)
    }

    fun e(message: String, exception: Exception? = null) {
        _logger.error(message, exception)
    }

    fun d(message: String) {
        _logger.debug(message)
    }
    fun i(message: String) {
        _logger.info(message)
    }
}