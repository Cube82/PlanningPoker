package pl.cube.planning_poker.models

import kotlinx.serialization.json.Json

object ProtocolJson {
    val instance: Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }
}
