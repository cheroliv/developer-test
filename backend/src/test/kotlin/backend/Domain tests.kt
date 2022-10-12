@file:Suppress(
    "NonAsciiCharacters",
    "unused",
    "ClassName",
)

package backend

import backend.Constants.DISTANCE
import backend.Constants.DISTANCE_LIMIT
import backend.Constants.PARENT
import backend.Constants.VISITE
import backend.Data.config
import backend.Data.expectedGraph
import backend.Data.routes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class `Domain tests` {



    @Test
    fun `list of destinations from routes`() {
        mutableListOf<String>().apply {
            addAll(routes.map { it.origin })
            addAll(routes.map { it.destination })
        }.toSet().run {
            assertEquals(setOf("Tatooine", "Dagobah", "Hoth", "Endor"), this)
            assertEquals(setOf("Tatooine", "Dagobah", "Hoth", "Endor"), routes.destinations)
        }
    }


    @Test
    fun `toGraph function return the destinations graph for a list of routes`() = with(routes) {
        expectedGraph.run {
            assertEquals(this, mutableMapOf<String, MutableMap<String, Int>>().apply {
                destinations.forEach { destination -> set(destination, mutableMapOf()) }
                forEach { route: Route ->
                    if (!this[route.origin]!!.containsKey(route.destination)) {
                        this[route.origin]!![route.destination] = route.travelTime
                    }
                    if (!this[route.destination]!!.containsKey(route.origin)) {
                        this[route.destination]!![route.origin] = route.travelTime
                    }
                }
            })
            assertEquals(this, routes.toGraph)
            assertEquals(toString(), routes.toGraph.toString())
        }
    }


    @Test
    fun `mini function a pair destination and travel time`() = assertEquals(
        Pair("Hoth", 1),
        mapOf("Endor" to 4, "Hoth" to 1, "Dagobah" to 6).mini
    )

    @Test
    fun `initialisation function`() = initialisation(routes.toGraph, config.departure).run {
        assertTrue(containsKey(DISTANCE))
        assertEquals(
            this[DISTANCE].toString(), mapOf(
                "Tatooine" to 0,
                "Dagobah" to DISTANCE_LIMIT,
                "Hoth" to DISTANCE_LIMIT,
                "Endor" to DISTANCE_LIMIT,
            ).toString()
        )

        assertTrue(containsKey(PARENT))
        mapOf(
            "Tatooine" to null,
            "Dagobah" to null,
            "Hoth" to null,
            "Endor" to null,
        ).apply { assertEquals(size, (this@run[PARENT] as Map<*, *>).size) }
            .map { assertTrue((this[PARENT] as Map<*, *>).containsKey(it.key)) }

        assertTrue(containsKey(VISITE))
        assertEquals(
            this[VISITE].toString(), listOf(
                "Tatooine",
                "Dagobah",
                "Hoth",
                "Endor",
            ).toString()
        )
    }

    @Test
    fun `shortestPath function`() = routes.shortestPath(
        config.departure,
        config.arrival
    ).run {
//        log.info(
//            "graph: ${
//                mapOf(
//                    "Tatooine" to mapOf("Dagobah" to 6, "Hoth" to 6),
//                    "Dagobah" to mapOf("Endor" to 4, "Hoth" to 1),
//                    "Hoth" to mapOf("Endor" to 1)
//                    // "Endor" to mapOf("Dagobah" to 4, "Hoth" to 1)
//                )
//            }"
//        )
//        log.info("shortestPath: $this")
    }
}
