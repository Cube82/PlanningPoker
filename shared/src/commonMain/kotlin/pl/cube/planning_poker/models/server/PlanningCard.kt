package pl.cube.planning_poker.models.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PlanningCard {
    @SerialName("0")
    Zero,

    @SerialName("1/2")
    Half,

    @SerialName("1")
    One,

    @SerialName("2")
    Two,

    @SerialName("3")
    Three,

    @SerialName("5")
    Five,

    @SerialName("8")
    Eight,

    @SerialName("13")
    Thirteen,

    @SerialName("20")
    Twenty,

    @SerialName("40")
    Forty,

    @SerialName("100")
    OneHundred,

    @SerialName("?")
    Question,
}
