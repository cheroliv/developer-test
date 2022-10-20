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
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.getBean
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import kotlin.test.*
import kotlin.text.Charsets.UTF_8


@ExtendWith(OutputCaptureExtension::class)
internal class `CLI tests` {

    @Test
    fun `check cli`(output: CapturedOutput): Unit = runBlocking {
        tripleExamples.map {
            with(cli(it.first, it.second)) {
                log.info(
                    "expectedOdds = ${
                        getBean<ObjectMapper>().readValue<Answer>(
                            getResource("classpath:${it.third}")
                                .file
                                .readText(UTF_8)
                        ).odds
                    }"
                )
                assertTrue(
                    output
                        .out
                        .contains("odds = ${getBean<RoadMapService>().giveMeTheOdds(it.first, it.second)}")
                )
            }
        }
    }
}