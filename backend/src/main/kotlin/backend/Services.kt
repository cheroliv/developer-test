@file:Suppress("unused")

package backend

import backend.Constants.SPRING_PROFILE_CLI
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBean
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
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
) {
    @PostConstruct
    private fun init() = runBlocking {
        checkProfileLog(context)
        loadOnBoardComputerConfig()
    }

    @Transactional
    private suspend fun loadOnBoardComputerConfig() = context
        .getBean<ObjectMapper>()
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
    suspend fun giveMeTheOdds(strEmpire: String): Double {
        val empire = context.getBean<ObjectMapper>().readValue<Empire>(strEmpire)
        return (-1).toDouble()
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
class Dijkstra(private val graph: AdjacencyList<Route>) {
    private fun route(
        destination: Vertex,
        paths: HashMap<Vertex, Visit>
    ): ArrayList<Edge> {
        var vertex = destination
        val path = arrayListOf<Edge>()
        loop@ while (true) {
            val visit = paths[vertex] ?: break
            when (visit.type) {
                VisitType.EDGE -> visit.edge?.let {
                    path.add(it)
                    vertex = it.source
                }

                VisitType.START -> break@loop
            }
        }
        return path
    }

    private fun distance(
        destination: Vertex,
        paths: HashMap<Vertex, Visit>
    ): Double {
        val path = route(destination, paths)
        return path.sumOf { it.weight ?: 0.0 }
    }

    fun shortestPath(start: Vertex): HashMap<Vertex, Visit> {
        val paths: HashMap<Vertex, Visit> = HashMap()
        paths[start]= Visit(VisitType.START)
        val distanceComparator= Comparator<Vertex> { first, second ->
            (distance(second, paths) - distance(first, paths)).toInt()
        }
        val priorityQueue=ComparatorPriorityQueueImpl(distanceComparator)
        priorityQueue.enqueue(start)
        return TODO("Provide the return value")
    }

    class ComparatorPriorityQueueImpl(distanceComparator: Comparator<Vertex>) {
        //TODO: add empire constraint
        fun enqueue(start: Vertex) {
            TODO("Not yet implemented")
        }

    }
}

class AdjacencyList<T> {

}

class Visit(
    val type: VisitType, val edge: Edge? =
        null
)

class Edge(val source: Vertex, val weight: Double?) {

}

enum class VisitType {
    START,
    EDGE
}

class Vertex {

}
