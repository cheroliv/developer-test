package backend

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.web.cors.CorsConfiguration


/*=================================================================================*/
@ConstructorBinding
@ConfigurationProperties(
    prefix = "backend",
    ignoreUnknownFields = false
)
@PropertySources(
    PropertySource(
        value = ["classpath:git.properties"],
        ignoreResourceNotFound = true
    ),
    PropertySource(
        value = ["classpath:META-INF/build-info.properties"],
        ignoreResourceNotFound = true
    )
)
class ApplicationProperties(
    val message: String,
    val item: String,
    val clientApp: ClientApp = ClientApp(),
    val database: Database = Database(),
    val http: Http = Http(),
    val cache: Cache = Cache(),
    val cors: CorsConfiguration = CorsConfiguration(),
) {
    class ClientApp(val name: String = "")
    class Database(val populatorPath: String = "")

    class Http(val cache: Cache = Cache()) {
        class Cache(val timeToLiveInDays: Int = 1461)
    }

    class Cache(val ehcache: Ehcache = Ehcache()) {
        class Ehcache(
            val timeToLiveSeconds: Int = 3600,
            val maxEntries: Long = 100
        )
    }

}