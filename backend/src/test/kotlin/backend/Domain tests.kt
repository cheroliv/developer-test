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
import backend.Log.log
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
    fun `graph function return the destinations graph for a list of routes`() = with(routes) {
        expectedGraph.run {
            assertEquals(this, mutableMapOf<String, MutableMap<String, Int>>().apply {
                destinations.forEach { destination -> set(destination, mutableMapOf()) }
                forEach { route: Route ->
                    if (!this[route.origin]!!.containsKey(route.destination))
                        this[route.origin]!![route.destination] = route.travelTime
                    if (!this[route.destination]!!.containsKey(route.origin))
                        this[route.destination]!![route.origin] = route.travelTime
                }
            })
            assertEquals(this, routes.graph)
            assertEquals(toString(), routes.graph.toString())
        }
    }


    @Test
    fun `mini function a pair destination and travel time`() = assertEquals(
        Pair("Hoth", 1),
        mapOf(
            "Endor" to 4,
            "Hoth" to 1,
            "Dagobah" to 6
        ).mini
    )

    @Test
    fun `initialisation function return data structure to consume from departure`() =
        initialisation(routes.graph, config.departure).run {
            assertTrue(containsKey(DISTANCE))
            assertEquals(
                this[DISTANCE].toString(),
                mapOf(
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
    fun `shortestPath function`() {
        val g: MutableMap<String, Any> = initialisation(routes.graph, config.departure)

//    while ((g[VISITE] as List<*>).isNotEmpty()) {
//        val chemins = mutableListOf<String>()
//        val u: Pair<String, Int> = mini(g[DISTANCE] as MutableMap<String, Int>)!!
//        var s: Pair<String, Int> = u
//        val m: Int = (g[DISTANCE] as MutableMap<String, Int>)[s.first]!!
//        while (s.first != source) {
//            chemins.add(u.first)
////            s=(g[PARENT] as MutableMap<String, Map<String, Int>>)[s].keys
//            s = Pair("", 1)
//            log.info(g[PARENT])
//        }
//    }
        val res = emptyMap<String, Any>()
        log.info("result: $res")
//        routes.shortestPath(
//            config.departure,
//            config.arrival
//        ).run {
//            log.info("shortestPath: $this")
////            assertEquals(res,this)
//        }
    }
}
