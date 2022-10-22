@file:Suppress(
    "NonAsciiCharacters",
    "unused",
    "RemoveExplicitTypeArguments",
    "ClassName",
)

package backend

import backend.Data.config
import backend.Data.pairExamples
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.bindToServer
import org.springframework.test.web.reactive.server.returnResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class `Backend tests` {
    private val client: WebTestClient by lazy {
        bindToServer().baseUrl(BASE_URL_DEV).build()
    }
    private lateinit var context: ConfigurableApplicationContext
    private val mapper: ObjectMapper by lazy { context.getBean<ObjectMapper>() }

    @BeforeAll
    fun `launch the onboard computer in profile test`() =
        runApplication<OnBoardComputerApplication> { bootstrap(app = this) }.run { context = this }

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
            assertTrue(contains("\"routes_db\": \"universe.db\""))

            context.getBean<ObjectMapper>().readValue<ComputerConfig>(this).run {
                assertEquals(6, autonomy)
                assertEquals("Tatooine", departure)
                assertEquals("Endor", arrival)
                assertEquals("universe.db", routesDb)
            }
        }
        assertEquals(5, countRoute(context), "universe must be persisted")
        with(findAllRoutes(context)) {
            sqliteRoutes(config.routesDb).map {
                assertTrue(
                    contains(it),
                    "let's compare retrieved data from database with what csv contains"
                )
            }
        }
    }


    @Test
    fun `Upload a JSON file containing the data intercepted by the rebels about the plans of the Empire and displaying the odds`() {

        pairExamples.map {
            client
                .post()
                .uri("api/give-me-the-odds")
                .contentType(APPLICATION_JSON)
                .bodyValue(
                    mapper.readValue<Empire>(context.getResource("classpath:${it.first}").file)
                )
                .exchange()
                .expectStatus()
                .isOk
                .returnResult<Int>().run {

                    val empireInRequest = mapper.readValue<Empire>(
                        context.getResource("classpath:${it.first}").file
                    )

                    val empireSent = mapper.readValue<Empire>(
                        requestBodyContent!!
                            .map { byte -> byte.toInt().toChar().toString() }
                            .reduce { acc: String, s: String -> acc + s }
                    )

                    assertEquals(empireInRequest, empireSent)

                    runBlocking {
                        responseBodyContent!!.apply {
                            val oddsResponseResult = map { byte -> byte.toInt().toChar().toString() }
                                .reduce { accumulator: String, s: String -> accumulator + s }
                                .toDouble()
                            val expectedOdds = mapper.readValue<Answer>(
                                context.getResource("classpath:${it.second}").file
                            ).odds
                            assertEquals(expectedOdds, oddsResponseResult)
                        }
                    }
                }
        }
    }
}
