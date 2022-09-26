@file:Suppress("unused")

package backend


import backend.Constants.SPRING_PROFILE_CLI
import backend.Constants.SPRING_PROFILE_CLI_PROPS
import backend.Log.log
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import kotlin.system.exitProcess



/*=================================================================================*/
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class Computer(
    private val context: ApplicationContext
) {
    @PostConstruct
    private fun init() = checkProfileLog(context)
}

/*=================================================================================*/

@Component
@Profile(SPRING_PROFILE_CLI)
class ComputerCommandLineRunner(
    private var context: ApplicationContext
) : CommandLineRunner, ApplicationContextAware {

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    override fun run(vararg args: String?) {
        //TODO: computation
        log.info("bean provided by spring container : ${context.beanDefinitionNames.toList()}")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            log.info("STARTING : Spring boot application starting")
            runApplication<Computer>(*args) {
                setAdditionalProfiles(SPRING_PROFILE_CLI)
                setDefaultProperties(SPRING_PROFILE_CLI_PROPS)
            }
            log.info("STOPPED  : Spring boot application stopped")
            exitProcess(0)
        }
    }
}

/*=================================================================================*/
