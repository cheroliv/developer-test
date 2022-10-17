@file:Suppress(
    "NonAsciiCharacters",
    "unused",
    "ClassName",
)

package backend

import backend.Data.config
import backend.Data.expectedRoadmap
import backend.Data.routes
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
}


