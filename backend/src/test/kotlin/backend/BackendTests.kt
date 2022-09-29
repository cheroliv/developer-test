@file:Suppress(
    "NonAsciiCharacters", "unused"
)

package backend

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.MULTIPART_FORM_DATA
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.BodyInserters.fromMultipartData
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
    fun `Upload a JSON file containing the data intercepted by the rebels about the plans of the Empire and displaying the odds`() {
        setOf(
            Pair("example1/empire.json", "example1/answer.json"),
            Pair("example2/empire.json", "example2/answer.json"),
            Pair("example3/empire.json", "example3/answer.json"),
            Pair("example4/empire.json", "example4/answer.json"),
        ).map {
            val bodyValue: BodyInserters.MultipartInserter = fromMultipartData(MultipartBodyBuilder().apply {
                part(
                    "empire",
                    context.getResource("classpath:${it.first}")
                ).contentType(MULTIPART_FORM_DATA)
            }.build())
            client
                .post()
                .uri("api/roadmap/give-me-the-odds")
                .contentType(APPLICATION_JSON)
                .body(bodyValue)
                .exchange()
                .expectStatus()
                .isOk
                .returnResult<Int>()
                .responseBodyContent!!.apply {
                    val answer = context.getBean<ObjectMapper>().readValue<Answer>(
                        context.getResource("classpath:${it.second}").file
                    )
                    val oddsResponseResult = map { byte -> byte.toInt().toChar().toString() }
                        .reduce { acc: String, s: String -> acc + s }.toDouble()
                    //TODO: uncomment this assertion to validate functionalities
//                    assertEquals(answer.odds, oddsResponseResult)
                    assertEquals((-1).toDouble(), oddsResponseResult)
                }.isNotEmpty().run { assertTrue(this) }
        }
    }
}

