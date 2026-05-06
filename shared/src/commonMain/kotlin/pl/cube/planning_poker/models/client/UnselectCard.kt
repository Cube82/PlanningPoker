package pl.cube.planning_poker.models.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("unselectCard")
data object UnselectCard : ClientMessage
