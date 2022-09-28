@file:Suppress("unused")

package backend


/*=================================================================================*/
data class Route(
    val origin: String,
    val destination: String,
//    @JsonProperty("travel_time")
    val travel_time: Int
)
/*=================================================================================*/
data class ComputerConfig    (
    val autonomy: Int,
    val departure: String,
    val arrival: String,
//    @JsonProperty("routes_db")
    val routes_db: String
)
/*=================================================================================*/
