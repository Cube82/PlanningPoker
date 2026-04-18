package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("serverError")
data class ServerError(
    val message: String,
    val code: ServerErrorCode,
) : ServerMessage
