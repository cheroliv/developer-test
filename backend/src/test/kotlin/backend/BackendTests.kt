@file:Suppress(
    "NonAsciiCharacters", "unused"
)

package backend

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BackendTests {
    private val client: WebTestClient by lazy {
        WebTestClient.bindToServer().baseUrl(BASE_URL_DEV).build()
    }
    private lateinit var context: ConfigurableApplicationContext

    @BeforeAll
    fun `launch the onboard computer in profile test`() =
        runApplication<OnBoardComputerApplication> { testLoader(app = this) }
            .run { context = this }

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
        //universe must be persisted
        assertEquals(5, countRoute(context))
        //let's compare retrieved data from database with what csv contains
        with(findAllRoutes(context)) {
            listOf(
                Route(origin = "Tatooine", destination = "Dagobah", travelTime = 6),
                Route(origin = "Dagobah", destination = "Endor", travelTime = 4),
                Route(origin = "Dagobah", destination = "Hoth", travelTime = 1),
                Route(origin = "Hoth", destination = "Endor", travelTime = 1),
                Route(origin = "Tatooine", destination = "Hoth", travelTime = 6),
            ).map { assertTrue(contains(it)) }
        }
    }

    @Test
    fun `upload a JSON file containing the data intercepted by the rebels about the plans of the Empire and displaying the odds`() {
        client
            .post()
            .uri("api/roadmap/give-me-the-odds")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk
            .returnResult<Int>()
            .responseBodyContent!!.apply {
                val oddsResponse = map { it.toInt().toChar().toString() }
                    .reduce { acc: String, s: String -> acc + s }.toInt()
                assertEquals(-1, oddsResponse)
            }.isNotEmpty().run { assertTrue(this) }
    }

    @Test
    @Ignore
    fun `Example 1, upload a JSON file containing the data intercepted by the rebels about the plans of the Empire and displaying the odds`() {
        client
            .post()
            .uri("api/roadmap/give-me-the-odds")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk
            .returnResult<Int>()
            .responseBodyContent!!.apply {
                val oddsResponse = map { it.toInt().toChar().toString() }
                    .reduce { acc: String, s: String -> acc + s }.toInt()
                assertEquals(-1, oddsResponse)
            }.isNotEmpty().run { assertTrue(this) }
    }

    @Test
    @Ignore
    fun `Example 2, upload a JSON file containing the data intercepted by the rebels about the plans of the Empire and displaying the odds`() {
        client
            .post()
            .uri("api/roadmap/give-me-the-odds")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk
            .returnResult<Int>()
            .responseBodyContent!!.apply {
                val oddsResponse = map { it.toInt().toChar().toString() }
                    .reduce { acc: String, s: String -> acc + s }.toInt()
                assertEquals(-1, oddsResponse)
            }.isNotEmpty().run { assertTrue(this) }
    }

    @Test
    @Ignore
    fun `Example 3, upload a JSON file containing the data intercepted by the rebels about the plans of the Empire and displaying the odds`() {
        client
            .post()
            .uri("api/roadmap/give-me-the-odds")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk
            .returnResult<Int>()
            .responseBodyContent!!.apply {
                val oddsResponse = map { it.toInt().toChar().toString() }
                    .reduce { acc: String, s: String -> acc + s }.toInt()
                assertEquals(-1, oddsResponse)
            }.isNotEmpty().run { assertTrue(this) }
    }

    @Test
    @Ignore
    fun `Example 4, upload a JSON file containing the data intercepted by the rebels about the plans of the Empire and displaying the odds`() {
        client
            .post()
            .uri("api/roadmap/give-me-the-odds")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk
            .returnResult<Int>()
            .responseBodyContent!!.apply {
                val oddsResponse = map { it.toInt().toChar().toString() }
                    .reduce { acc: String, s: String -> acc + s }.toInt()
                assertEquals(-1, oddsResponse)
            }.isNotEmpty().run { assertTrue(this) }
    }

}

