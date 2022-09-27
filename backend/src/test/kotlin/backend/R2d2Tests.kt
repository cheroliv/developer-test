@file:Suppress(
    "NonAsciiCharacters", "unused"
)

package backend

import backend.Constants.SPRING_PROFILE_CLI
import backend.Constants.SPRING_PROFILE_CLI_PROPS
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import kotlin.test.Test
import kotlin.test.assertTrue

@ExtendWith(OutputCaptureExtension::class)
internal class R2d2Tests {
    private lateinit var context: ConfigurableApplicationContext
    private val dao: R2dbcEntityTemplate by lazy { context.getBean() }

    private fun launchCli(vararg args: String) = runApplication<OnBoardComputerApplication>(*args) {
        testLoader(this)
        setAdditionalProfiles(SPRING_PROFILE_CLI)
        setDefaultProperties(SPRING_PROFILE_CLI_PROPS)
    }.run { context = this }

//    @Test
//    @kotlin.test.Ignore
//    fun `CLI canary test using output capture`(output: CapturedOutput) {
//        launchCli()
//        log.info("bean provided by spring container : ${context.beanDefinitionNames.toList()}")
//        assertTrue(output.out.contains(
//            OnBoardComputerApplication::class.simpleName!!.run {
//                return@run replaceRange(0, 1, first().lowercase())
//            }
//        ))
//    }

    @Test
    fun `CLI canary bootstrap test`() {
        launchCli()
        assertTrue(context.containsBean(
            OnBoardComputerApplication::class.simpleName!!.run {
                replaceRange(
                    0,
                    1,
                    first().lowercase()
                )
            }
        ))
    }
}