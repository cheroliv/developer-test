@file:Suppress("unused")

package backend

import backend.Constants.SPRING_PROFILE_CLI
import backend.Log.log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.StandardCharsets.UTF_8
import javax.annotation.PostConstruct

/*=================================================================================*/

@Service
class RoadMapService(
    @Value("classpath:millennium-falcon.json")
    private val configurationFile: Resource,
    private val routeRepository: RouteRepository,
    private val context: ApplicationContext,
    private val objectMapper: ObjectMapper,
) {
    @PostConstruct
    private fun init() = runBlocking {
        checkProfileLog(context)
        loadOnBoardComputerConfig()
    }

    @Transactional
    private suspend fun loadOnBoardComputerConfig() = objectMapper
        .readValue<ComputerConfig>(configurationFile.file)
        .run { routeRepository.saveAll(readUniverseCsv(routesDb)) }

    private fun readUniverseCsv(fileName: String): List<Route> = context
        .getResource("classpath:${fileName}")
        .file
        .readText(UTF_8)
        .lines()
        .drop(1)
        .map {
            it.split(";").run {
                Route(
                    origin = this[0],
                    destination = this[1],
                    travelTime = this[2].toInt(),
                )
            }
        }

    @Transactional(readOnly = true)
    suspend fun giveMeTheOdds(strEmpire: String, routes: List<Route>): Double {
        val empire = objectMapper.readValue<Empire>(strEmpire)
        return -1.0
    }

    @Transactional(readOnly = true)
    suspend fun giveMeTheOdds(strEmpire: String): Double {
        val empire = objectMapper.readValue<Empire>(strEmpire)
        return -1.0
    }
}
/*=================================================================================*/

@Component
@Profile(SPRING_PROFILE_CLI)
class OnBoardComputerCliRunner(
    private val context: ApplicationContext,
    private val mapper: ObjectMapper
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val config = mapper.readValue<ComputerConfig>(
            context.getResource("classpath:${args.first()}")
                .file.readText(Charsets.UTF_8)
        )

        val routes: List<Route> = context
            .getResource("classpath:${config.routesDb}")
            .file
            .readText(UTF_8)
            .lines()
            .drop(1)
            .map {
                it.split(";").run {
                    Route(
                        origin = first(),
                        destination = this[1],
                        travelTime = last().toInt(),
                    )
                }
            }
        val odds = giveMeTheOdds(
            routes.roadmap,
            config,
            mapper.readValue(
                context.getResource("classpath:${args.last()}")
                    .file.readText(UTF_8)
            ),
            shortestPath(
                routes.graph,
                config.departure,
                config.arrival
            )
        )
        log.info("odds = $odds")
    }

}
/*=================================================================================*/

