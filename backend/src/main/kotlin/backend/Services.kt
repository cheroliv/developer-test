@file:Suppress("unused")

package backend

import backend.Constants.SPRING_PROFILE_CLI
import com.fasterxml.jackson.databind.ObjectMapper
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


/*=================================================================================*/

@Service
@Transactional
class RoadMapService(
    @Value("classpath:millennium-falcon.json")
    private val configurationFile: Resource,
    private val routeRepository: RouteRepository,
    private val mapper: ObjectMapper,
//    private val csvMapper: CsvMapper,
    private val context: ApplicationContext,
) {
    @PostConstruct
    private fun init() = runBlocking {
        checkProfileLog(context)
        loadOnBoardComputerConfig()
    }

    private suspend fun loadOnBoardComputerConfig() {
        //read json on classpath read ComputerConfig
        val conf: ComputerConfig = mapper.readValue(
            configurationFile.file, ComputerConfig::class.java
        )
        //read csv on classpath
//        val routes: List<Route> = readUniverseCsv(conf.routesDb).apply {
//            map { println(it) }
//        }
//        println(context.getResource("classpath:${conf.routes_db}").file.readText(UTF_8))

//        routeRepository.insertAll(routes)


        listOf(
            Route(origin = "Tatooine", destination = "Dagobah", travelTime = 6),
            Route(origin = "Dagobah", destination = "Endor", travelTime = 4),
            Route(origin = "Dagobah", destination = "Hoth", travelTime = 1),
            Route(origin = "Hoth", destination = "Endor", travelTime = 1),
            Route(origin = "Tatooine", destination = "Hoth", travelTime = 6),
        ).run {
            routeRepository.saveAll(this)
        }

//        readUniverseCsv(conf.routesDb).map { println(it) }
    }

//    private fun readUniverseCsv(fileName: String): List<Route> = try {
//        CsvMapper()
//            .readerFor(Route::class.java)
//            .with(CsvSchema.emptySchema().withHeader().withSkipFirstDataRow(true))
//            .readValues<Route>(
//                context
//                    .getResource("classpath:${fileName}")
//                    .file
//                    .readText(UTF_8)
//            ).readAll()
//
//    } catch (e: Exception) {
//        log.error("Error occurred while loading object list from file $fileName", e)
//        emptyList()
//    }


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
