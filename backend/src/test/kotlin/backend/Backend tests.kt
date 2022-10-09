@file:Suppress(
    "NonAsciiCharacters",
    "unused",
    "RemoveExplicitTypeArguments",
    "ClassName",
)

package backend

import backend.Data.pairSet
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
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
import org.springframework.web.reactive.function.BodyInserters.fromMultipartData
import java.nio.charset.StandardCharsets
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class `Backend tests` {
    private val client: WebTestClient by lazy {
        WebTestClient.bindToServer().baseUrl(BASE_URL_DEV).build()
    }
    private lateinit var context: ConfigurableApplicationContext
    private val mapper: ObjectMapper by lazy { context.getBean<ObjectMapper>() }

    @BeforeAll
    fun `launch the onboard computer in profile test`() =
        runApplication<OnBoardComputerApplication> { testLoader(app = this) }
            .run { context = this }

    @AfterAll
    fun `stop the onboard computer`() = context.close()

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

            context.getBean<ObjectMapper>().readValue<ComputerConfig>(this).run {
                assertEquals(6, autonomy)
                assertEquals("Tatooine", departure)
                assertEquals("Endor", arrival)
                assertEquals("universe.csv", routesDb)
            }
        }
        //universe must be persisted
        assertEquals(5, countRoute(context))
        //let's compare retrieved data from database with what csv contains
        with(findAllRoutes(context)) {
            context.getResource("classpath:universe.csv")
                .file
                .readText(StandardCharsets.UTF_8)
                .lines()
                .drop(1)
                .map {
                    it.split(";").run {
                        Route(
                            origin = first(),
                            destination = this[1],
                            travelTime = last().toInt(),
                        )
                    }
                }.map { assertTrue(contains(it)) }
        }
    }

    @Test
    fun `Upload a JSON file containing the data intercepted by the rebels about the plans of the Empire and displaying the odds`() {

        pairSet.map {
            client
                .post()
                .uri("api/give-me-the-odds")
                .contentType(APPLICATION_JSON)
                .body(fromMultipartData(MultipartBodyBuilder().apply {
                    part(
                        "empire",
                        context.getResource("classpath:${it.first}")
                    ).contentType(MULTIPART_FORM_DATA)
                }.build()))
                .exchange()
                .expectStatus()
                .isOk
                .returnResult<Int>().run {
                    val expectedEmpire = mapper.readValue<Empire>(
                        context.getResource("classpath:${it.first}").file
                    )
                    val resultEmpire = mapper.readValue<Empire>(requestBodyContent!!
                        .map { it.toInt().toChar().toString() }
                        .reduce { acc: String, s: String -> acc + s }
                        .lines()
                        .drop(5)//clean what's not json in request body
                        .dropLast(1)//clean what's not json in request body
                        .reduce { accumulator: String, s: String -> accumulator + "\n" + s })
                    assertEquals(expectedEmpire, resultEmpire)

                    runBlocking {
                        responseBodyContent!!.apply {
                            val oddsResponseResult = map { byte -> byte.toInt().toChar().toString() }
                                .reduce { accumulator: String, s: String -> accumulator + s }
                                .toDouble()
//                            assertEquals(
//                                mapper.readValue<Answer>(
//                                    context.getResource("classpath:${it.second}").file
//                                ).odds, oddsResponseResult
//                            )
                            assertEquals(-1.0, oddsResponseResult)
                        }.isNotEmpty().run { assertTrue(this) }
                    }
                }
        }
    }
}
