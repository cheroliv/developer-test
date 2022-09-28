@file:Suppress(
    "NonAsciiCharacters", "unused"
)

package backend

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test
import kotlin.test.assertTrue

internal class BackendTests {
    private val client: WebTestClient by lazy {
        WebTestClient.bindToServer().baseUrl(BASE_URL_DEV).build()
    }
    private lateinit var context: ConfigurableApplicationContext
    private val dao: R2dbcEntityTemplate by lazy { context.getBean() }

    @BeforeAll
    fun `launch the onboard computer in profile test`() =
        runApplication<OnBoardComputerApplication> { testLoader(app = this) }.run { context = this }

    @AfterAll
    fun `stop the onboard computer`() = context.close()

    /**
     * universe.csv:
     * origin;      destination;    travel_time
     * Tatooine;    Dagobah;        6
     * Dagobah;     Endor;          4
     * Dagobah;     Hoth;           1
     * Hoth;        Endor;          1
     * Tatooine;    Hoth;           6
     */
    @Test
    fun `When it starts, the back-end service will read a JSON configuration file containing the autonomy`() {
        //validate needed resources are on classpath and contains expected values
        with(context.getResource("classpath:millennium-falcon.json").file.readText(Charsets.UTF_8)) {
            assertTrue(contains("{"))
            assertTrue(contains("}"))
            assertTrue(contains("\"autonomy\": 6,"))
            assertTrue(contains("\"departure\": \"Tatooine\","))
            assertTrue(contains("\"arrival\": \"Endor\","))
            assertTrue(contains("\"routes_db\": \"universe.csv\""))
        }
//        //universe must be persisted
//        assertEquals(5, countRoute(dao))
//        //let's compare retrieved data from database with what csv contains
//        with(findAllRoutes(dao)) {
//            listOf(
//                Route(origin = "Tatooine", destination = "Dagobah", travel_time = 6),
//                Route(origin = "Dagobah", destination = "Endor", travel_time = 4),
//                Route(origin = "Dagobah", destination = "Hoth", travel_time = 1),
//                Route(origin = "Hoth", destination = "Endor", travel_time = 1),
//                Route(origin = "Tatooine", destination = "Hoth", travel_time = 6),
//            ).map { assertTrue(contains(it)) }
//        }
        println("countRoute: ${countRoute(dao)}")
    }
}

