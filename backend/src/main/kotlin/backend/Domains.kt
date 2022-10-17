@file:Suppress(
    "unused",
    "SpellCheckingInspection",
    "UnusedReceiverParameter",
    "PARAMETER_NAME_CHANGED_ON_OVERRIDE",
)

package backend


import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.Double.Companion.POSITIVE_INFINITY

/*=================================================================================*/
fun <T, E : Number> shortestPath(graph: IGraph<T, E>, from: T, destination: T)
        : Pair<List<T>, Double> = dijkstra(
    graph, from, destination
)[destination] ?: (emptyList<T>() to POSITIVE_INFINITY)
/*=================================================================================*/

private fun <T, E : Number> dijkstra(
    graph: IGraph<T, E>,
    from: T,
    destination: T? = null
): Map<T, Pair<List<T>, Double>> {
    val unvisitedSet = graph.getAllVertices().toMutableSet()
    val distances = graph.getAllVertices().associateWith { POSITIVE_INFINITY }.toMutableMap()
    val paths = mutableMapOf<T, List<T>>()
    distances[from] = 0.0
    var current = from
    while (unvisitedSet.isNotEmpty() && unvisitedSet.contains(destination)) {
        graph.adjacentVertices(current).forEach { adjacent ->
            val distance = graph.getDistance(current, adjacent).toDouble()
            if (distances[current]!! + distance < distances[adjacent]!!) {
                distances[adjacent] = distances[current]!! + distance
                paths[adjacent] = paths.getOrDefault(current, listOf(current)) + listOf(adjacent)
            }
        }
        unvisitedSet.remove(current)
        if (current == destination
            || unvisitedSet.all { distances[it]!!.isInfinite() }
        ) break
        if (unvisitedSet.isNotEmpty())
            current = unvisitedSet.minBy { distances[it]!! }!!
    }
    return paths.mapValues { entry -> entry.value to distances[entry.key]!! }
}
/*=================================================================================*/

data class Edge<T, E : Number>(val from: T, val to: T, val value: E)
/*=================================================================================*/

interface IGraph<T, E : Number> {
    fun getAllVertices(): Set<T>
    fun adjacentVertices(vertex: T): Set<T>
    fun getDistance(from: T, to: T): E
}

/*=================================================================================*/

class Graph<T, E : Number>(
    val directed: Boolean = false,
    val defaultCost: E
) : IGraph<T, E> {
    private val edges = mutableSetOf<Edge<T, E>>()
    private val vertices = mutableSetOf<T>()

    fun addVertex(node: T): Graph<T, E> {
        vertices += node
        return this
    }

    fun addArc(pair: Pair<T, T>, value: E? = null): Graph<T, E> {
        val (from, to) = pair
        addVertex(from)
        addVertex(to)
        addRelation(from, to, value ?: defaultCost)
        if (!directed) addRelation(to, from, value ?: defaultCost)
        return this
    }

    override fun getAllVertices(): Set<T> = vertices.toSet()

    private fun addRelation(from: T, to: T, value: E) {
        edges += Edge(from, to, value)
    }

    override fun adjacentVertices(from: T): Set<T> = edges
        .filter { it.from == from }
        .map { it.to }
        .toSet()

    override fun getDistance(from: T, to: T): E = edges
        .filter { it.from == from && it.to == to }
        .map { it.value }
        .first()
}

/*=================================================================================*/
val List<Route>.graph
    get() = Graph<String, Int>(
        directed = true,
        defaultCost = 1
    ).apply {
        roadmap.forEach { departure ->
            departure.value.forEach { destination ->
                addArc(Pair(departure.key, destination.key), destination.value)
            }
        }
    }

/*=================================================================================*/
val List<Route>.destinations: Set<String>
    get() = mutableListOf<String>().apply {
        addAll(this@destinations.map { it.origin })
        addAll(this@destinations.map { it.destination })
    }.toSet()

/*=================================================================================*/
val List<Route>.roadmap: MutableMap<String, MutableMap<String, Int>>
    get() = mutableMapOf<String, MutableMap<String, Int>>().apply {
        destinations.forEach { destination -> set(destination, mutableMapOf()) }
        forEach { route: Route ->
            if (!this[route.origin]!!.containsKey(route.destination))
                this[route.origin]!![route.destination] = route.travelTime
            if (!this[route.destination]!!.containsKey(route.origin))
                this[route.destination]!![route.origin] = route.travelTime
        }
    }
/*=================================================================================*/

data class Route(
    val origin: String,
    val destination: String,
    @JsonProperty("travel_time")
    val travelTime: Int
)

/*=================================================================================*/
data class ComputerConfig(
    val autonomy: Int,
    val departure: String,
    val arrival: String,
    @JsonProperty("routes_db")
    val routesDb: String
)

/*=================================================================================*/
data class Answer(val odds: Double)

/*=================================================================================*/
data class Empire(
    val countdown: Int,
    @JsonProperty("bounty_hunters")
    val bountyHunters: List<BountyHunter>
)

/*=================================================================================*/
data class BountyHunter(
    val planet: String,
    val day: Int
)
/*=================================================================================*/
