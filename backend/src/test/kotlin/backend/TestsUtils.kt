@file:Suppress("unused")

package backend

import backend.Constants.SPRING_PROFILE_CONF_DEFAULT_KEY
import backend.Constants.SPRING_PROFILE_TEST
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate

const val BASE_URL_DEV = "http://localhost:8080"

fun bootstrap(app: SpringApplication) = with(app) {
    setDefaultProperties(
        hashMapOf<String, Any>().apply {
            set(
                SPRING_PROFILE_CONF_DEFAULT_KEY,
                SPRING_PROFILE_TEST
            )
        })
    setAdditionalProfiles(SPRING_PROFILE_TEST)
}

fun cli(vararg args: String) = runApplication<OnBoardComputerApplication>(*args) {
    bootstrap(this)
    setAdditionalProfiles(Constants.SPRING_PROFILE_CLI)
    setDefaultProperties(Constants.SPRING_PROFILE_CLI_PROPS)
}

fun countRoute(context: ApplicationContext): Int = when {
    context.getBean("routeRepository") is RouteRepositoryInMemory ->
        runBlocking {
            context.getBean<RouteRepositoryInMemory>().findAllRoutes().size
        }

    else -> context.getBean<R2dbcEntityTemplate>()
        .select(RouteEntity::class.java)
        .count()
        .block()?.toInt()!!
}

fun findAllRoutes(context: ApplicationContext): List<Route> = when {
    context.getBean("routeRepository") is RouteRepositoryInMemory ->
        runBlocking {
            context.getBean<RouteRepositoryInMemory>().findAllRoutes()
        }

    else -> context.getBean<R2dbcEntityTemplate>()
        .select(RouteEntity::class.java)
        .all()
        .toIterable()
        .map { it.toDomain }
}