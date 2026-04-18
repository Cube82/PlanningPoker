package pl.cube.planning_poker.logger

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object Logger {
    fun e(exception: Exception)
    fun e(message: String, exception: Exception? = null)
    fun d(message: String)
    fun i(message: String)
}