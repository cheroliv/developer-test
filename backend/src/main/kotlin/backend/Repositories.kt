@file:Suppress("unused")

package backend

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository

/*=================================================================================*/
interface RouteRepository {
    suspend fun saveAll(routes: List<Route>)
    suspend fun findAllRoutes(): List<Route>
}
/*=================================================================================*/

//@Repository
class RouteRepositoryR2dbc(
    private val dao: R2dbcEntityTemplate
) : RouteRepository {
    override suspend fun saveAll(routes: List<Route>) {
        routes.map {
            dao.insert(RouteEntity(it))
                .awaitSingle()
        }
    }

    override suspend fun findAllRoutes(): List<Route> {
        TODO("Not yet implemented")
    }
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
        fun saveAll(routes: List<Route>) = InMemoryData.routes.addAll(routes)
    }

    override suspend fun saveAll(routes: List<Route>) {
        InMemoryData.saveAll(routes)
    }

    override suspend fun findAllRoutes(): List<Route> = InMemoryData.findAllRoutes

}
/*=================================================================================*/
