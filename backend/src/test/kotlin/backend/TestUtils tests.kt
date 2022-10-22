@file:Suppress("ClassName")

package backend

import backend.Data.config
import backend.Data.routes
import backend.Data.universeCsv
import kotlin.test.Test
import kotlin.test.assertEquals


internal class `TestUtils tests` {

    @Test
    fun `testing csv reader`() = routes.forEachIndexed { i, it ->
        readUniverseCsv(universeCsv).run { assertEquals(it, this[i]) }
    }

    @Test
    fun `testing sqlite reader`() = routes.forEachIndexed { i, it ->
        sqliteRoutes(config.routesDb).run { assertEquals(it, this[i]) }
    }

}