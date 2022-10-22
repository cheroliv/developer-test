@file:Suppress("unused")

package backend

import backend.Constants.PROFILE_CLI
import backend.Log.log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBean
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import kotlin.text.Charsets.UTF_8


/*=================================================================================*/
@Service
class RoadMapService(
    @Value("classpath:millennium-falcon.json")
    private val configurationFile: Resource,
    private val routeRepository: RouteRepository,
    private val context: ApplicationContext,
    private val mapper: ObjectMapper,
) {

    private val config: ComputerConfig by lazy { mapper.readValue(configurationFile.file) }

    @PostConstruct
    @Transactional
    private fun init() = runBlocking {
        checkProfileLog(context)
        routeRepository.saveAll(sqliteRoutes(config.routesDb))
    }

    @Transactional(readOnly = true)
    suspend fun giveMeTheOdds(configPath: String, empirePath: String): Double {
        val config: ComputerConfig = mapper.readValue(
            context.getResource("classpath:$configPath")
                .file.readText(UTF_8)
        )
        val empire: Empire = mapper.readValue(
            context.getResource("classpath:$empirePath")
                .file.readText(UTF_8)
        )
        val routes: List<Route> = sqliteRoutes(config.routesDb)

        return giveMeTheOdds(
            routes.roadmap,
            config,
            empire,
            shortestPath(
                routes.graph,
                config.departure,
                config.arrival
            )
        )
    }

    @Transactional(readOnly = true)
    suspend fun giveMeTheOdds(
        empire: Empire
    ): Double = with(routeRepository.findAllRoutes()) {
        giveMeTheOdds(
            roadmap,
            config,
            empire,
            shortestPath(
                graph,
                config.departure,
                config.arrival
            )
        )
    }
}

/*=================================================================================*/
@Component
@Profile(PROFILE_CLI)

class OnBoardComputerCliRunner(
    private val context: ApplicationContext,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        runBlocking {
            log.info(context.getBean<RoadMapService>().giveMeTheOdds(args.first()!!, args.last()!!))
        }
    }
}
/*=================================================================================*/

