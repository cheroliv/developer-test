@file:Suppress(
    "NonAsciiCharacters", "unused", "ClassName"
)

package backend

import backend.Constants.SPRING_PROFILE_CLI
import backend.Constants.SPRING_PROFILE_CLI_PROPS
import backend.Data.tripleSet
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue


@ExtendWith(OutputCaptureExtension::class)
internal class `CLI tests` {
    private lateinit var context: ConfigurableApplicationContext
    private val dao by lazy { context.getBean<R2dbcEntityTemplate>() }
    private val mapper by lazy { context.getBean<ObjectMapper>() }

    private fun launchCli(vararg args: String) =
        runApplication<OnBoardComputerApplication>(*args) {
            testLoader(this)
            setAdditionalProfiles(SPRING_PROFILE_CLI)
            setDefaultProperties(SPRING_PROFILE_CLI_PROPS)
        }.run { context = this }

    @Test
    fun `cli men at work`(output: CapturedOutput) {
        tripleSet.map {
            launchCli(it.first, it.second)
            assertTrue(output.out.contains("odds = -1"))
        }
    }

    @Test @Ignore
    fun `check cli`(output: CapturedOutput) {
        tripleSet.map {
            launchCli(it.first, it.second)
            assertTrue(
                output.out.contains(
                    "odds = ${
                        mapper.readValue<Answer>(
                            context.getResource("classpath:${it.third}")
                                .file
                                .readText(Charsets.UTF_8)
                        ).odds
                    }"
                )
            )
        }
    }
}