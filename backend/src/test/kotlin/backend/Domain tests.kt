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
import backend.Data.routes
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.system.OutputCaptureExtension
import kotlin.test.*


internal class `Domain tests` {

    @Test
    fun `toGraph extension function who converts a list of Route to a graph`() {
        with(routes) {
            mutableMapOf<String, Map<String, Int>>().apply {
                groupBy { it.origin }
                    .map { node ->
                        mapOf(node.key to node.value.map { route ->
                            mapOf(route.destination to route.travelTime)
                        })
                    }.flatMap {
                        it.entries
                    }.map { entry ->
                        this[entry.key] = mutableMapOf<String, Int>().apply {
                            entry.value.map {
                                it.entries.map { it1 -> set(it1.key, it1.value) }
                            }
                        }
                    }
            }.run {
                toGraph.let {
                    assertEquals(toString(), it.toString())
                    assertEquals(
                        mapOf(
                            "Tatooine" to mapOf("Dagobah" to 6, "Hoth" to 6),
                            "Dagobah" to mapOf("Endor" to 4, "Hoth" to 1),
                            "Hoth" to mapOf("Endor" to 1)
                        ).toString(), it.toString(),
                        "only possible with initial universe.csv"
                    )
                }
            }
        }
    }


    @Test
    fun `mini function`() {
        assertEquals(
            Pair("Hoth", 1),
            mini(mapOf("Endor" to 4, "Hoth" to 1, "Dagobah" to 6))
        )
    }

    @Test
    fun `initialisation function`() {

        initialisation(routes.toGraph, config.departure).run {
            assertTrue(containsKey(DISTANCE))
            assertTrue(containsKey(PARENT))
            assertTrue(containsKey(VISITE))

            assertEquals(
                this[DISTANCE].toString(), mapOf(
                    "Tatooine" to 0,
                    "Dagobah" to DISTANCE_LIMIT,
                    "Hoth" to DISTANCE_LIMIT
                ).toString()
            )

            mapOf(
                "Tatooine" to null,
                "Dagobah" to null,
                "Hoth" to null
            ).map {
                assertTrue((this[PARENT] as Map<*, *>).containsKey(it.key))
            }

            assertEquals(
                this[VISITE].toString(), listOf(
                    "Tatooine",
                    "Dagobah",
                    "Hoth"
                ).toString()
            )
        }
    }

    @Test
    fun `dijkstra function`() {
        dijkstra(
            routes.toGraph,
            config.departure,
            config.arrival
        )
    }
}
