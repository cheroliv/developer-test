@file:Suppress(
    "NonAsciiCharacters",
    "unused",
    "ClassName",
)

package backend

import backend.Data.tripleExamples
import backend.Log.log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.getBean
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.context.ConfigurableApplicationContext
import java.nio.charset.StandardCharsets
import kotlin.test.*
import kotlin.text.Charsets.UTF_8


@ExtendWith(OutputCaptureExtension::class)
internal class `CLI tests` {
    private lateinit var context: ConfigurableApplicationContext
    private val mapper by lazy { context.getBean<ObjectMapper>() }


    @Test
    fun `check cli`(output: CapturedOutput) {
        tripleExamples.map {
            context = cli(it.first, it.second)
            val config = mapper.readValue<ComputerConfig>(
                context.getResource("classpath:${it.first}")
                    .file.readText(UTF_8)
            )
            val empire = mapper.readValue<Empire>(
                context.getResource("classpath:${it.second}")
                    .file.readText(UTF_8)
            )
            val expectedOdds = mapper.readValue<Answer>(
                context.getResource("classpath:${it.third}")
                    .file
                    .readText(UTF_8)
            ).odds

            val universe: List<Route> = context
                .getResource("classpath:${config.routesDb}")
                .file
                .readText(StandardCharsets.UTF_8)
                .lines()
                .drop(1)
                .map { line ->
                    line.split(";").run {
                        Route(
                            origin = first(),
                            destination = this[1],
                            travelTime = last().toInt(),
                        )
                    }
                }

            val odds = giveMeTheOdds(
                universe.roadmap,
                config,
                empire,
                shortestPath(
                    universe.graph,
                    config.departure,
                    config.arrival
                )
            )
//            assertTrue(output.out.contains("odds = $expectedOdds"))
                        log.info("expectedOdds = $expectedOdds")

        }
    }
}