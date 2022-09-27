@file:Suppress("unused")

package backend


/*=================================================================================*/
data class Route(
    val origin: String,
    val destination: String,
    val travelTime: Int
)

/*=================================================================================*/
data class ComputerConfig(
    val autonomy: Int,
    val departure: String,
    val arrival: String,
    val routesDb: String
)