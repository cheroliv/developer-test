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
        WebTestClient
            .bindToServer()
            .baseUrl(BASE_URL_DEV)
            .build()
    }
    private lateinit var context: ConfigurableApplicationContext
    private val dao: R2dbcEntityTemplate by lazy { context.getBean() }

    @BeforeAll
    fun `launch the onboard computer in profile test`() =
        runApplication<OnBoardComputerApplication> { testLoader(app = this) }
            .run { context = this }

    @AfterAll
    fun `stop the onboard computer`() = context.close()

    @Test
    fun `When it starts, the back-end service will read a JSON configuration file containing the autonomy`() {
        val universeFileName = "universe.csv"
        val universeCsv =
            """origin;destination;travel_time
                Tatooine;Dagobah;6
                Dagobah;Endor;4
                Dagobah;Hoth;1
                Hoth;Endor;1
                Tatooine;Hoth;6"""
        val millenniumFalconJsonFileName = "millennium-falcon.json"
        val millenniumFalconJson =
            """{
                "autonomy": 6,
                "departure": "Tatooine",
                "arrival": "Endor",
                "routes_db": "universe.csv"
            }"""
        assertTrue(context.getResource("classpath:$universeFileName").isFile)
        assertTrue(context.getResource("classpath:$millenniumFalconJsonFileName").isFile)
        println(context.getResource("classpath:$universeFileName").file.toString())
//        assertTrue(context.getResource("classpath:$millenniumFalconJsonFileName").isFile)

    }
}

