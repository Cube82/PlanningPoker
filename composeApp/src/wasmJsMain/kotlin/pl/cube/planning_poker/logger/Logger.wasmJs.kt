package pl.cube.planning_poker.logger

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Logger {
    actual fun e(exception: Exception) {
        println("ERROR: [PlanningPoker] throwable: ${exception.message}")
    }

    actual fun e(message: String, exception: Exception?) {
        if (exception != null) {
            println("ERROR: [PlanningPoker] $message. Throwable: ${exception.message}")
        } else {
            println("ERROR: [PlanningPoker] $message")
        }
    }

    actual fun d(message: String) {
        println("DEBUG: [PlanningPoker] $message")
    }

    actual fun i(message: String) {
        println("INFO: [PlanningPoker] $message")
    }
}