@file:Suppress("unused")

package backend

import backend.Constants.CSV_DELIMITER
import backend.Constants.SPRING_PROFILE_CLI
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
import kotlin.text.Charsets.UTF_8
import javax.annotation.PostConstruct

/*=================================================================================*/
@Service
class RoadMapService(
    @Value("classpath:millennium-falcon.json")
    private val configurationFile: Resource,
    private val routeRepository: RouteRepository,
    private val context: ApplicationContext,
    private val mapper: ObjectMapper,
) {
    @PostConstruct
    private fun init() = runBlocking {
        checkProfileLog(context)
        loadOnBoardComputerConfig()
    }

    @Transactional
    private suspend fun loadOnBoardComputerConfig() = mapper
        .readValue<ComputerConfig>(configurationFile.file)
        .run { routeRepository.saveAll(readUniverseCsv(routesDb)) }

    private fun readUniverseCsv(fileName: String): List<Route> = context
        .getResource("classpath:${fileName}")
        .file
        .readText(UTF_8)
        .lines()
        .drop(1)
        .map {
            it.split(CSV_DELIMITER).run {
                Route(
                    origin = this[0],
                    destination = this[1],
                    travelTime = this[2].toInt(),
                )
            }
        }

    @Transactional(readOnly = true)
    suspend fun giveMeTheOdds(strConfig: String, strEmpire: String): Double {
        val config = mapper.readValue<ComputerConfig>(
            context.getResource("classpath:$strConfig")
                .file.readText(Charsets.UTF_8)
        )
        val routes: List<Route> = context
            .getResource("classpath:${config.routesDb}")
            .file
            .readText(UTF_8)
            .lines()
            .drop(1)
            .map {
                it.split(CSV_DELIMITER).run {
                    Route(
                        origin = first(),
                        destination = this[1],
                        travelTime = last().toInt(),
                    )
                }
            }
        return giveMeTheOdds(
            routes.roadmap,
            config,
            mapper.readValue(
                context.getResource("classpath:$strEmpire")
                    .file.readText(UTF_8)
            ),
            shortestPath(
                routes.graph,
                config.departure,
                config.arrival
            )
        )
    }

    @Transactional(readOnly = true)
    suspend fun giveMeTheOdds(strEmpire: String): Double {
        val empire = mapper.readValue<Empire>(strEmpire)
        val config = mapper.readValue<ComputerConfig>(configurationFile.file)
        val routes = routeRepository.findAllRoutes()
        return giveMeTheOdds(
            routes.roadmap,
            config,
            empire,
            shortestPath(
                routes.graph,
                config.departure, config.arrival
            )
        )

    }
}
/*=================================================================================*/

@Component
@Profile(SPRING_PROFILE_CLI)
class OnBoardComputerCliRunner(
    private val context: ApplicationContext,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        runBlocking {
            log.info(
                "odds = ${
                    context.getBean<RoadMapService>().giveMeTheOdds(
                        args.first().toString(),
                        args.last().toString()
                    )
                }"
            )
        }
    }
}
/*=================================================================================*/

