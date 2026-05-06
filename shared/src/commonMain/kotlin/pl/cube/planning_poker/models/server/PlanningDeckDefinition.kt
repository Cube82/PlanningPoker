package pl.cube.planning_poker.models.server

data class PlanningDeckDefinition(
    val id: String,
    val cards: List<PlanningCard>,
)

object PlanningDecks {
    val Fibonacci = PlanningDeckDefinition(
        id = "fibonacci",
        cards = listOf(
            PlanningCard.Zero,
            PlanningCard.Half,
            PlanningCard.One,
            PlanningCard.Two,
            PlanningCard.Three,
            PlanningCard.Five,
            PlanningCard.Eight,
            PlanningCard.Thirteen,
            PlanningCard.Twenty,
            PlanningCard.Forty,
            PlanningCard.OneHundred,
            PlanningCard.Question,
        ),
    )
}

fun PlanningCard.displayLabel(): String = when (this) {
    PlanningCard.Zero -> "0"
    PlanningCard.Half -> "½"
    PlanningCard.One -> "1"
    PlanningCard.Two -> "2"
    PlanningCard.Three -> "3"
    PlanningCard.Five -> "5"
    PlanningCard.Eight -> "8"
    PlanningCard.Thirteen -> "13"
    PlanningCard.Twenty -> "20"
    PlanningCard.Forty -> "40"
    PlanningCard.OneHundred -> "100"
    PlanningCard.Question -> "?"
}
