@file:Suppress(
    "NonAsciiCharacters",
    "unused",
    "ClassName",
)

package backend

import backend.Data.tripleExamples
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.getBean
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.context.ConfigurableApplicationContext
import kotlin.test.*
import kotlin.text.Charsets.UTF_8


@ExtendWith(OutputCaptureExtension::class)
internal class `CLI tests` {
    private lateinit var context: ConfigurableApplicationContext
    private val mapper by lazy { context.getBean<ObjectMapper>() }


    @Test
    fun `men at work check cli`(output: CapturedOutput) {
        tripleExamples.map {
            cli(it.first, it.second)
            assertTrue(output.out.contains("odds = -1"))
        }
    }

    @Test
    @Ignore
    fun `check cli`(output: CapturedOutput) {
        tripleExamples.map {
            context = cli(it.first, it.second)
            val expectedOdds = mapper.readValue<Answer>(
                context.getResource("classpath:${it.third}")
                    .file
                    .readText(UTF_8)
            ).odds
            assertTrue(output.out.contains("odds = $expectedOdds"))
        }
    }
}