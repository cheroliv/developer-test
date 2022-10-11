@file:Suppress(
    "unused",
    "SpellCheckingInspection",
    "UNCHECKED_CAST",
    "UNUSED_PARAMETER",
)

package backend

import backend.Constants.DISTANCE
import backend.Constants.DISTANCE_LIMIT
import backend.Constants.PARENT
import backend.Constants.VISITE
import backend.Log.log
import com.fasterxml.jackson.annotation.JsonProperty

/*=================================================================================*/

/*=================================================================================*/
val List<Route>.toGraph: Map<String, Map<String, Int>>
    get() = mutableMapOf<String, Map<String, Int>>().apply {
        groupBy { it.origin }
            .map { (origin, routes) ->
                mapOf(origin to routes.map { (_, destination, travelTime) ->
                    mapOf(destination to travelTime)
                })
            }.flatMap { it.entries }
            .forEach { (origin, routes) ->
                this[origin] = mutableMapOf<String, Int>().apply {
                    routes.forEach {
                        it.entries.forEach { (destination, travelTime) -> set(destination, travelTime) }
                    }
                }
            }
    }

/*=================================================================================*/
fun initialisation(
    graphe: Map<String, Map<String, Int>>,
    source: String,
): Map<String, Any> = mutableMapOf<String, Any>().apply {
    val du = mutableMapOf<String, Int>()
    val parentu = mutableMapOf<String, Map<String, Int>>()
    val v = mutableListOf<String>()

    graphe.forEach {
        du[it.key] = DISTANCE_LIMIT
        parentu[it.key] = emptyMap()
        v.add(it.key)
    }

    du[source] = 0
    this[DISTANCE] = du
    this[PARENT] = parentu
    this[VISITE] = v
}

/*=================================================================================*/
fun mini(d: Map<String, Int>): Pair<String, Int>? {
    d.forEach { node ->
        if (d.minBy { it.value }.value == d[node.key])
            return Pair(node.key, node.value)
    }
    return null
}

/*=================================================================================*/
fun relachement(
    graph: Map<String, Any>,
    u: String,
    v: String,
    d: MutableMap<String, Int>,
    p: MutableMap<String, Any>
): MutableMap<String, Any> = mutableMapOf<String, Any>().apply {
    if (d[v]!! > d[u]!! + (graph[u] as Map<String, Int>)[v]!!) {
        d[v] = d[u]!! + (graph[u] as Map<String, Int>)[v]!!
        p[v] = u
    }
    this[DISTANCE] = d[v]!!
    this[PARENT] = u
}

/*=================================================================================*/
fun dijkstra(
    graphe: Map<String, Map<String, Int>>,
    source: String,
    destination: String
) {
    val g: MutableMap<String, Any> = initialisation(graphe, source) as MutableMap<String, Any>

//    Log.log.info(u)
    repeat(5) {
//    while ((g[VISITE] as List<String>).isNotEmpty()) {
        val chemins = mutableListOf<String>()
        val u: Pair<String, Int> = mini(g[DISTANCE] as MutableMap<String, Int>)!!
        var s: Pair<String, Int> = u
        val m: Int = (g[DISTANCE] as MutableMap<String, Int>)[s.first]!!
//        Log.log.info(m)
        repeat(5) {
//        while (s.first != source) {
            chemins.add(u.first)
//            s=(g[PARENT] as MutableMap<String, Map<String, Int>>)[s].keys
            s = Pair("", 1)
            log.info("toto")
        }
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
