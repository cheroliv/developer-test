@file:Suppress(
    "NonAsciiCharacters", "unused", "ClassName"
)

package backend

import backend.Constants.SPRING_PROFILE_CLI
import backend.Constants.SPRING_PROFILE_CLI_PROPS
import backend.Data.tripleSet
import com.fasterxml.jackson.databind.ObjectMapper
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
internal class `CLI tests` {
    private lateinit var context: ConfigurableApplicationContext
    private val dao by lazy { context.getBean<R2dbcEntityTemplate>() }
    private val mapper by lazy { context.getBean<ObjectMapper>() }

    private fun launchCli(vararg args: String) =
        runApplication<OnBoardComputerApplication>(*args) {
            testLoader(this)
            setAdditionalProfiles(SPRING_PROFILE_CLI)
            setDefaultProperties(SPRING_PROFILE_CLI_PROPS)
        }.run { context = this }

    @Test
    fun `check cli`(output: CapturedOutput) {
        tripleSet.map {
            launchCli(it.first, it.second)
            assertTrue(output.out.contains("odds = -1"))
        }
    }

//    @Test
//    fun `check cli`(output: CapturedOutput) {
//        tripleSet.map {
//            launchCli(it.first, it.second)
//            assertTrue(
//                output.out.contains(
//                    "odds = ${
//                        mapper.readValue<Answer>(
//                            context.getResource("classpath:${it.third}")
//                                .file
//                                .readText(Charsets.UTF_8)
//                        ).odds
//                    }"
//                )
//            )
//        }
//    }
}



//class Dijkstra(private val graph: AdjacencyList<Route>) {
//    private fun route(
//        destination: Vertex,
//        paths: HashMap<Vertex, Visit>
//    ): ArrayList<Edge> {
//        var vertex = destination
//        val path = arrayListOf<Edge>()
//        loop@ while (true) {
//            val visit = paths[vertex] ?: break
//            when (visit.type) {
//                VisitType.EDGE -> visit.edge?.let {
//                    path.add(it)
//                    vertex = it.source
//                }
//
//                VisitType.START -> break@loop
//            }
//        }
//        return path
//    }
//
//    private fun distance(
//        destination: Vertex,
//        paths: HashMap<Vertex, Visit>
//    ): Double {
//        val path = route(destination, paths)
//        return path.sumOf { it.weight ?: 0.0 }
//    }
//
//    fun shortestPath(start: Vertex): HashMap<Vertex, Visit> {
//        val paths: HashMap<Vertex, Visit> = HashMap()
//        paths[start]= Visit(VisitType.START)
//        val distanceComparator= Comparator<Vertex> { first, second ->
//            (distance(second, paths) - distance(first, paths)).toInt()
//        }
//        val priorityQueue=ComparatorPriorityQueueImpl(distanceComparator)
//        priorityQueue.enqueue(start)
//        return TODO("Provide the return value")
//    }
//
//    class ComparatorPriorityQueueImpl(distanceComparator: Comparator<Vertex>) {
//        //TODO: add empire constraint
//        fun enqueue(start: Vertex) {
//            TODO("Not yet implemented")
//        }
//
//    }
//}
//
//class AdjacencyList<T> {
//
//}
//
//class Visit(
//    val type: VisitType, val edge: Edge? =
//        null
//)
//
//class Edge(val source: Vertex, val weight: Double?) {
//
//}
//
//enum class VisitType {
//    START,
//    EDGE
//}
//
//class Vertex {
//
//}