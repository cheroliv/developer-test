@file:Suppress("unused")

package backend

import com.fasterxml.jackson.annotation.JsonProperty


/*=================================================================================*/
data class Route(
    val origin: String,
    val destination: String,
    @JsonProperty("travel_time")
    val travelTime: Int
)
/*=================================================================================*/
data class ComputerConfig    (
    val autonomy: Int,
    val departure: String,
    val arrival: String,
    @JsonProperty("routes_db")
    val routesDb: String
)
/*=================================================================================*/
