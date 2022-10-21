@file:Suppress("unused")

package backend


import backend.Constants.NORMAL_TERMINATION
import backend.Constants.PROFILE_CLI
import backend.Constants.PROFILE_CLI_PROPS
import backend.Constants.PROFILE_CONF_DEFAULT_KEY
import backend.Constants.PROFILE_DEVELOPMENT
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import java.util.*
import kotlin.system.exitProcess

/*=================================================================================*/
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class OnBoardComputerApplication


/*=================================================================================*/
object OnBoardComputerBootstrap {
    @JvmStatic
    fun main(args: Array<String>) = runApplication<OnBoardComputerApplication>(*args) {
        setAdditionalProfiles(PROFILE_DEVELOPMENT)
        setDefaultProperties(hashMapOf<String, Any>(PROFILE_CONF_DEFAULT_KEY to PROFILE_DEVELOPMENT))
    }.run { bootstrapLog(context = this) }

}
/*=================================================================================*/

object OnBoardComputerCliBootstrap {
    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<OnBoardComputerApplication>(*args) {
            setAdditionalProfiles(PROFILE_CLI)
            setDefaultProperties(PROFILE_CLI_PROPS)
        }
        exitProcess(NORMAL_TERMINATION)
    }
}
/*=================================================================================*/
