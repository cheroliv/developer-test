@file:Suppress("unused")

package backend

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono


/*=================================================================================*/
interface RouteRepository {
    suspend fun insertAll(routes: List<Route>)
}
/*=================================================================================*/

@Repository
class RouteRepositoryR2dbc(
    private val dao: R2dbcEntityTemplate
) : RouteRepository {
    override suspend fun insertAll(routes: List<Route>) {
        routes.forEach {
            dao.insert<RouteEntity>(RouteEntity(it)).awaitSingle()
            //TODO: avec sql client
        }
    }
}
/*=================================================================================*/
