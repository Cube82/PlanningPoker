package pl.cube.planning_poker.models.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("revealCards")
data object RevealCards : ClientMessage
