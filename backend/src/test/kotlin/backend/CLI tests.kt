@file:Suppress(
    "NonAsciiCharacters",
    "unused",
    "ClassName",
)

package backend

import backend.Data.tripleSet
import backend.Log.log
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

    //TODO: test isolated case 1
    //{distance={Tatooine=0, Dagobah=100000000, Hoth=100000000}, parent={Tatooine={}, Dagobah={}, Hoth={}}, visite=[Tatooine, Dagobah, Hoth]}
    @Test
    fun `initialisation function`(output: CapturedOutput) {
        tripleSet.map { triple ->
            context = cli(triple.first, triple.second)

            val graph = findAllRoutes(context).toGraph

            val source = mapper.readValue<ComputerConfig>(
                context
                    .getResource("classpath:${triple.first}")
                    .file
                    .readText(UTF_8)
            ).departure

            initialisation(graph, source)
        }
    }

    @Test
    fun `check mini function`() {
        assertEquals(
            Pair("Hoth", 1),
            mini(mapOf("Endor" to 4, "Hoth" to 1, "Dagobah" to 6))
        )
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