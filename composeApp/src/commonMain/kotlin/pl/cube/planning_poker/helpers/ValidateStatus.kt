package pl.cube.planning_poker.helpers

sealed interface ValidateStatus {
    data object Valid : ValidateStatus
    data class Invalid(val reason: PlayerNameValidationError) : ValidateStatus
}

enum class PlayerNameValidationError {
    InvalidLength,
}
