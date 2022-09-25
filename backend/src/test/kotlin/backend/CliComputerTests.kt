@file:Suppress(
    "NonAsciiCharacters", "unused"
)

package backend

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import kotlin.test.Test
import kotlin.test.assertTrue

@ExtendWith(OutputCaptureExtension::class)
class CliComputerTests {
    private lateinit var context: ConfigurableApplicationContext
    private val dao: R2dbcEntityTemplate by lazy { context.getBean() }

    private fun launchCli(vararg args: String) = runApplication<Computer>(*args) {
        testLoader(this)
        setAdditionalProfiles("cli")
        setDefaultProperties(
            mutableMapOf<String, Any>(
                "spring.main.web-application-type" to "none"
            )
        )
    }.run { context = this }

    @Test
    fun `Canary cli`(output: CapturedOutput) {
        launchCli()
        assertTrue(output.out.contains(Computer::class.simpleName!!.lowercase()))
    }
}