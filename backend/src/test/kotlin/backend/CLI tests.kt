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
import backend.Data.tripleSet
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.getBean
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import kotlin.test.*
import kotlin.text.Charsets.UTF_8


@ExtendWith(OutputCaptureExtension::class)
internal class `CLI & Domain tests` {
    private lateinit var context: ConfigurableApplicationContext
    private val dao by lazy { context.getBean<R2dbcEntityTemplate>() }
    private val mapper by lazy { context.getBean<ObjectMapper>() }


    @Test
    fun `men at work check cli`(output: CapturedOutput) {
        tripleSet.map {
            cli(it.first, it.second)
            assertTrue(output.out.contains("odds = -1"))
        }
    }

    @Test
    @Ignore
    fun `check cli`(output: CapturedOutput) {
        tripleSet.map {
            context = cli(it.first, it.second)
            val expectedOdds = mapper.readValue<Answer>(
                context.getResource("classpath:${it.third}")
                    .file
                    .readText(UTF_8)
            ).odds
            assertTrue(output.out.contains("odds = $expectedOdds"))
        }
    }

    @Test
    fun `toGraph extension function who converts a list of Route to a graph`() {
        tripleSet.map { triple ->
            with(findAllRoutes(cli(triple.first, triple.second))) {
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
        tripleSet.map { example ->
            context = cli(example.first, example.second)

            val graph = findAllRoutes(context).toGraph

            val source = mapper.readValue<ComputerConfig>(
                context
                    .getResource("classpath:${example.first}")
                    .file
                    .readText(UTF_8)
            ).departure

            initialisation(graph, source).run {
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
    }


    @Test
    fun `dijkstra function`() {
        tripleSet.map { triple ->
            context = cli(triple.first, triple.second)
            val config = mapper.readValue<ComputerConfig>(
                context
                    .getResource("classpath:${triple.first}")
                    .file
                    .readText(UTF_8)
            )
            val graph = findAllRoutes(context).toGraph
            val source = config.departure
            val destination = config.arrival

            dijkstra(graph, source, destination)
        }
    }
}