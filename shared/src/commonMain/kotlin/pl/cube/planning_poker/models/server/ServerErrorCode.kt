package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ServerErrorCode {
    @SerialName("join:userNameTaken")
    UserNameTaken,

    @SerialName("join:tableNotFound")
    TableNotFound,
}
