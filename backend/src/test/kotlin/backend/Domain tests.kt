@file:Suppress(
    "NonAsciiCharacters",
    "unused",
    "ClassName",
)

package backend

import backend.Constants.UNLUCKY
import backend.Data.config
import backend.Data.expectedRoadmap
import backend.Data.routes
import backend.Log.log
import kotlin.test.Test
import kotlin.test.assertEquals


internal class `Domain tests` {
    @Test
    fun `list of destinations from routes`() {
        mutableListOf<String>().apply {
            addAll(routes.map { it.origin })
            addAll(routes.map { it.destination })
        }.toSet().run destinations@{
            with(setOf("Tatooine", "Dagobah", "Hoth", "Endor")) expected@{
                assertEquals(this@expected, this@destinations)
                assertEquals(this@expected, routes.destinations)
            }
        }
    }

    @Test
    fun `roadmap function return the destinations map for a list of routes`() = with(routes) {
        expectedRoadmap.run {
            assertEquals(this, mutableMapOf<String, MutableMap<String, Int>>().apply {
                destinations.forEach { destination -> set(destination, mutableMapOf()) }
                forEach { route: Route ->
                    if (!this[route.origin]!!.containsKey(route.destination))
                        this[route.origin]!![route.destination] = route.travelTime
                    if (!this[route.destination]!!.containsKey(route.origin))
                        this[route.destination]!![route.origin] = route.travelTime
                }
            })
            assertEquals(this, routes.roadmap)
            assertEquals(toString(), routes.roadmap.toString())
        }
    }

    @Test
    fun `dijkstra algorithm`() {
        val (path, value) = shortestPath(
            routes.graph,
            config.departure,
            config.arrival
        )
        assertEquals(listOf("Tatooine", "Hoth", "Endor"), path)
        assertEquals(7.0, value)
        println("path = $path, value = $value")
    }


    @Test
    fun `give me the odds`() {
        val result: Pair<List<String>, Double> = shortestPath(
            routes.graph,
            config.departure,
            config.arrival
        ).apply {
            log.info("shortestPath: $this")
        }
        val empire: Empire = Empire(
            countdown = 7,
            bountyHunters = listOf(
                BountyHunter(planet = "Hoth", day = 6),
                BountyHunter(planet = "Hoth", day = 7),
                BountyHunter(planet = "Hoth", day = 8)
            )
        )
        assertEquals(giveMeTheOdds(result, empire), UNLUCKY)
    }

    private fun giveMeTheOdds(
        path: Pair<List<String>, Double>,
        empire: Empire
    ): Double {
        if (path.second > empire.countdown) return UNLUCKY
        if (empire.countdown < refuelTime(path)) return UNLUCKY
        return 1.0 - odds(path,empire)
    }

    private fun odds(path: Pair<List<String>, Double>, empire: Empire): Double {
        TODO("Not yet implemented")
    }

    private fun refuelTime(path: Pair<List<String>, Double>): Int {
        var timeWithRefuel = 0
        var currentAutonomy = config.autonomy
        val size = path.first.size
        var cptRefuel = 0
        path.first.forEachIndexed { index, it ->
            if (size - index > 1) {
                val timeToNext: Int = routes.roadmap[it]!![path.first[index + 1]]!!
                if (timeToNext >= currentAutonomy) {
                    currentAutonomy = config.autonomy
                    cptRefuel++
                } else {
                    currentAutonomy -= timeToNext
                }
                timeWithRefuel += timeToNext
                log.info("timeToNext(${it}, ${path.first[index + 1]}): $timeToNext")
            }
        }
        return timeWithRefuel + cptRefuel
    }
}


