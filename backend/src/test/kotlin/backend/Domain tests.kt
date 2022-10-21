@file:Suppress("ClassName")

package backend

import backend.Constants.UNLUCKY
import backend.Data.config
import backend.Data.expectedRoadmap
import backend.Data.routes
import kotlin.math.pow
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
    }


    @Test
    fun `give me the odds`() = assertEquals(
        giveMeTheOdds(
            routes.roadmap,
            config,
            Empire(
                countdown = 7,
                bountyHunters = listOf(
                    BountyHunter(planet = "Hoth", day = 6),
                    BountyHunter(planet = "Hoth", day = 7),
                    BountyHunter(planet = "Hoth", day = 8)
                )
            ),
            shortestPath(
                routes.graph,
                config.departure,
                config.arrival
            )
        ), UNLUCKY
    )

    @Test
    fun `odds function`() = repeat((0..4).count()) {
        with(odds(it)) {
            if (it == 0) assertEquals(0.0, this)
            if (it == 1) assertEquals(
                9.0.pow(1 - 1) / 10.0.pow(1),
                this
            )
            if (it == 2) assertEquals(
                9.0.pow(1 - 1) / 10.0.pow(1)
                        + 9.0.pow(2 - 1) / 10.0.pow(2),
                this
            )
            if (it == 3) assertEquals(
                9.0.pow(1 - 1) / 10.0.pow(1)
                        + 9.0.pow(2 - 1) / 10.0.pow(2)
                        + 9.0.pow(3 - 1) / 10.0.pow(3),
                this
            )
            if (it == 4) assertEquals(
                9.0.pow(1 - 1) / 10.0.pow(1)
                        + 9.0.pow(2 - 1) / 10.0.pow(2)
                        + 9.0.pow(3 - 1) / 10.0.pow(3)
                        + 9.0.pow(4 - 1) / 10.0.pow(4),
                this
            )
        }
    }

    @Test
    fun `graph property function`() {
        Log.log.info("roadmap: ${routes.roadmap}")
        Log.log.info("graph: ${routes.graph}")
    }

}