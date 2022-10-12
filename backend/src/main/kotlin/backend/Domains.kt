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
import com.fasterxml.jackson.annotation.JsonProperty

/*=================================================================================*/
val List<Route>.destinations: Set<String>
    get() = mutableListOf<String>().apply {
        addAll(this@destinations.map { it.origin })
        addAll(this@destinations.map { it.destination })
    }.toSet()


/*=================================================================================*/
val List<Route>.toGraph: Map<String, Map<String, Int>>
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
val Map<String, Int>.mini: Pair<String, Int>?
    get() {
        forEach { node ->
            if (minBy { it.value }.value == this[node.key])
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
fun List<Route>.shortestPath(
    source: String,
    destination: String
): Map<Pair<String, String>, Map<String, Any>> {
    val g: MutableMap<String, Any> = initialisation(toGraph, source) as MutableMap<String, Any>

//    while ((g[VISITE] as List<*>).isNotEmpty()) {
//        val chemins = mutableListOf<String>()
//        val u: Pair<String, Int> = mini(g[DISTANCE] as MutableMap<String, Int>)!!
//        var s: Pair<String, Int> = u
//        val m: Int = (g[DISTANCE] as MutableMap<String, Int>)[s.first]!!
//        while (s.first != source) {
//            chemins.add(u.first)
////            s=(g[PARENT] as MutableMap<String, Map<String, Int>>)[s].keys
//            s = Pair("", 1)
//            log.info(g[PARENT])
//        }
//    }
    return emptyMap()
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
