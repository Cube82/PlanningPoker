package pl.cube.planning_poker.models.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("joinTable")
data class JoinTable(
    val userName: String,
    val tableId: String,
) : ClientMessage
