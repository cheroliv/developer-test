@file:Suppress("unused")

package backend

import java.util.*


/*=================================================================================*/
interface RouteRecord

/*=================================================================================*/
data class RouteEntity(
    var id: UUID?,
    val origin: String,
    val destination: String,
    val travelTime: Int,
) : RouteRecord
/*=================================================================================*/
