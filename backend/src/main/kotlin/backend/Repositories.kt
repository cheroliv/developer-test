@file:Suppress("unused")

package backend

import backend.RouteRepositoryInMemory.InMemoryData.routes
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.select
import org.springframework.stereotype.Repository

/*=================================================================================*/
interface RouteRepository {
    suspend fun saveAll(routes: List<Route>)
    suspend fun findAllRoutes(): List<Route>
}
/*=================================================================================*/

//@Repository("routeRepository")
class RouteRepositoryR2dbc(
    private val dao: R2dbcEntityTemplate
) : RouteRepository {
    override suspend fun saveAll(routes: List<Route>) {
        routes.map {
            dao.insert(RouteEntity(it))
                .awaitSingle()
        }
    }

    override suspend fun findAllRoutes(): List<Route> = dao
        .select<RouteEntity>()
        .all()
        .map { it.toDomain }
        .toIterable()
        .toList()
}

/*=================================================================================*/
@Repository("routeRepository")
class RouteRepositoryInMemory : RouteRepository {
    private object InMemoryData {
        private val routes: MutableList<Route> by lazy {
            emptyList<Route>().toMutableList()
        }

        @JvmStatic
        val findAllRoutes: List<Route>
            get() = routes

        @JvmStatic
        fun saveAll(routes: List<Route>) = with(InMemoryData.routes) {
            clear()
            addAll(routes)
        }
    }

    override suspend fun saveAll(routes: List<Route>) {
        InMemoryData.saveAll(routes)
    }

    override suspend fun findAllRoutes(): List<Route> = InMemoryData.findAllRoutes

}
/*=================================================================================*/
