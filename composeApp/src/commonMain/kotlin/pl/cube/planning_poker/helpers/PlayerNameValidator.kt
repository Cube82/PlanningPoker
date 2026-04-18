package pl.cube.planning_poker.helpers

internal class PlayerNameValidator {
    fun isPlayerNameValid(playerName: String): ValidateStatus {
        val isValid = playerName.isNotBlank() && playerName.length in 3..20

        return if (isValid) {
            ValidateStatus.Valid
        } else {
            ValidateStatus.Invalid(PlayerNameValidationError.InvalidLength)
        }
    }
}
