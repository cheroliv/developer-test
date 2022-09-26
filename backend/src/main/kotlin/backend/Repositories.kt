@file:Suppress("unused")

package backend

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository



/*=================================================================================*/
interface RouteRepository
/*=================================================================================*/

@Repository
class RouteRepositoryR2dbc(
    private val dao: R2dbcEntityTemplate
) : RouteRepository
/*=================================================================================*/
