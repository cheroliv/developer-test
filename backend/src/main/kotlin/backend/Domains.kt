@file:Suppress(
    "unused",
    "SpellCheckingInspection",
    "UNCHECKED_CAST",
    "UNUSED_PARAMETER",
)

package backend

import backend.Constants.DISTANCE
import backend.Constants.PARENT
import backend.Constants.VISITE
import com.fasterxml.jackson.annotation.JsonProperty

/*=================================================================================*/
fun initialisation(
    config: ComputerConfig,
    routes: List<Route>,
): Map<String, Any> = mutableMapOf<String, Any>().apply {
    val du = mutableMapOf<String, Int>()
    val parentu = mutableMapOf<String, Map<String, Int>>()
    val v = mutableListOf<String>()

    routes.forEach {
        du[it.origin] = 100000000
        parentu[it.origin] = emptyMap()
        v.add(it.origin)
    }

    du[config.departure] = 0
    this[DISTANCE] = du
    this[PARENT] = parentu
    this[VISITE] = v
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
fun mini(d: MutableMap<String, Int>): Int? {
    d.minBy { it.value }.run {
        d.map { if (this.value == d[it.key]) return it.value }
    }
    return null
}
/*=================================================================================*/
fun dijsktra(
    graphe: MutableMap<String, Any>,
    source: String,
    destination: String
) {
//    val g = initialisation(graphe, source)

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
