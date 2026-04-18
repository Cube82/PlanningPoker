package pl.cube.planning_poker.logger

import android.util.Log

private const val TAG = "PP"
private const val UNKNOWN_ERROR = "unknown error"

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Logger {
    actual fun e(exception: Exception) {
        Log.e(TAG, exception.message ?: UNKNOWN_ERROR)
    }

    actual fun e(message: String, exception: Exception?) {
        if (exception != null) {
            Log.e(TAG, message, exception)
        } else {
            Log.e(TAG, message)
        }
    }

    actual fun d(message: String) {
        Log.d(TAG, message)
    }

    actual fun i(message: String) {
        Log.i(TAG, message)
    }
}