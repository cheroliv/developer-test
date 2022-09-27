@file:Suppress("unused")

package backend

import backend.Constants.SPRING_PROFILE_CONF_DEFAULT_KEY
import backend.Constants.SPRING_PROFILE_TEST
import kotlinx.coroutines.runBlocking
import org.springframework.boot.SpringApplication
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.awaitExists
import reactor.kotlin.adapter.rxjava.toFlowable

const val BASE_URL_DEV = "http://localhost:8080"

fun testLoader(app: SpringApplication) = with(app) {
    setDefaultProperties(
        hashMapOf<String, Any>().apply {
            set(
                SPRING_PROFILE_CONF_DEFAULT_KEY,
                SPRING_PROFILE_TEST
            )
        })
    setAdditionalProfiles(SPRING_PROFILE_TEST)
}

fun countRoute(dao: R2dbcEntityTemplate): Int =
    dao.select(RouteEntity::class.java).count().block()?.toInt()!!

fun findAllRoutes(dao: R2dbcEntityTemplate): List<Route> =
   dao.select(RouteEntity::class.java).all()
       .toIterable()
       .map { it.toDomain() }
