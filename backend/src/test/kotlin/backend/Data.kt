package backend

object Data {
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

    val pairsExample = setOf(
        Pair("example1/empire.json", "example1/answer.json"),
        Pair("example2/empire.json", "example2/answer.json"),
        Pair("example3/empire.json", "example3/answer.json"),
        Pair("example4/empire.json", "example4/answer.json"),
    )

    val triplesExample = pairsExample.map {
        Triple(
            it.first.replace(
                "empire.json",
                "millennium-falcon.json"
            ),
            it.first,
            it.second
        )
    }.toSet()
}