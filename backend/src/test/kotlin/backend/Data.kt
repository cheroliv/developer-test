package backend

object Data{
    val pairSet = setOf(
        Pair("example1/empire.json", "example1/answer.json"),
        Pair("example2/empire.json", "example2/answer.json"),
        Pair("example3/empire.json", "example3/answer.json"),
        Pair("example4/empire.json", "example4/answer.json"),
    )

    val tripleSet = pairSet.map {
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