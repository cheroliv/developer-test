package backend

object Data {
    val pairExamples = setOf(
        Pair("example1/empire.json", "example1/answer.json"),
        Pair("example2/empire.json", "example2/answer.json"),
        Pair("example3/empire.json", "example3/answer.json"),
        Pair("example4/empire.json", "example4/answer.json"),
    )

    val tripleExamples = pairExamples.map {
        Triple(
            it.first.replace("empire.json", "millennium-falcon.json"),
            it.first,
            it.second
        )
    }.toSet()

    val expectedRoadmap = mapOf(
        "Tatooine" to mapOf("Dagobah" to 6, "Hoth" to 6),
        "Dagobah" to mapOf("Tatooine" to 6, "Endor" to 4, "Hoth" to 1),
        "Hoth" to mapOf("Dagobah" to 1, "Endor" to 1, "Tatooine" to 6),
        "Endor" to mapOf("Dagobah" to 4, "Hoth" to 1)
    )


    val routes = listOf(
        Route("Tatooine", "Dagobah", 6),
        Route("Dagobah", "Endor", 4),
        Route("Dagobah", "Hoth", 1),
        Route("Hoth", "Endor", 1),
        Route("Tatooine", "Hoth", 6),
    )

    val config = ComputerConfig(
        autonomy = 6,
        departure = "Tatooine",
        arrival = "Endor",
        routesDb = "universe.csv"
    )
}