@file:Suppress("unused")

package backend

import backend.Constants.SPRING_PROFILE_CLI
import backend.Log.log
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import kotlin.text.Charsets.UTF_8


/*=================================================================================*/

@Service
@Transactional
class RoadMapService(
    private val routeRepository: RouteRepository,
    private val context: ApplicationContext,
    @Value("classpath:millennium-falcon.json")
    private val configurationFile: Resource

) {
    //    val computerConfig: ComputerConfig by lazy { readComputerConfig() }
//    private final fun readComputerConfig(): ComputerConfig {
//        TODO("")
//    }

    private suspend fun loadOnBoardComputerConfig() {
        log.info("not yet implemented")
        //read json on classpath read ComputerConfig
//read csv on classpath
        configurationFile.file.readText(UTF_8).run { println(this) }
    }

    @PostConstruct
    private fun init() = checkProfileLog(context).run {
        runBlocking { loadOnBoardComputerConfig() }
    }

}
/*=================================================================================*/

@Component
@Profile(SPRING_PROFILE_CLI)
class OnBoardComputerCliRunner(
    private var context: ApplicationContext,
) : CommandLineRunner, ApplicationContextAware {
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    override fun run(vararg args: String?) {
        //TODO: computation
    }

}
/*=================================================================================*/
