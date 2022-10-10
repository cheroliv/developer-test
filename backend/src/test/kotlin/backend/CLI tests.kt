@file:Suppress(
    "NonAsciiCharacters", "unused", "ClassName"
)

package backend

import backend.Constants.SPRING_PROFILE_CLI
import backend.Constants.SPRING_PROFILE_CLI_PROPS
import backend.Data.tripleSet
import backend.Log.log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
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

    private fun cli(vararg args: String) =
        runApplication<OnBoardComputerApplication>(*args) {
            bootstrap(this)
            setAdditionalProfiles(SPRING_PROFILE_CLI)
            setDefaultProperties(SPRING_PROFILE_CLI_PROPS)
        }.run { context = this }

    @Test @Ignore
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
            cli(it.first, it.second)
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
            cli(triple.first, triple.second)
            with(findAllRoutes(context)) {
                groupBy { it.origin }
                    .map { item: Map.Entry<String, List<Route>> ->
                        mapOf(item.key to item.value.map { route: Route ->
                            mapOf(route.destination to route.travelTime)
                        })
                    }.run { assertEquals(toString(), toGraph.toString()) }
            }
        }
    }

    @Test
    fun `initialisation function`(output: CapturedOutput) {
        tripleSet.map { triple ->
            cli(triple.first, triple.second)
            initialisation(
                findAllRoutes(context).toGraph,
                mapper.readValue<ComputerConfig>(
                    context
                        .getResource("classpath:${triple.first}")
                        .file
                        .readText(UTF_8)
                ).departure
            ).run {
                log.info(this)
//{'distance': {'A': 0}, 'parent': {}, 'visite': []}
//{distance={Tatooine=0, Dagobah=100000000, Hoth=100000000}, parent={Tatooine={}, Dagobah={}, Hoth={}}, visite=[Tatooine, Dagobah, Hoth]}
            }
        }
    }
}