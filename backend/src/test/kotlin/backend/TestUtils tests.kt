@file:Suppress("ClassName")

package backend

import backend.Data.config
import backend.Data.routes
import backend.Data.universeCsv
import kotlin.test.Test
import kotlin.test.assertEquals


internal class `TestUtils tests` {
    @Test
    fun `testing sqlite reader`() = sqliteRoutes(config.routesDb).run {
        routes.forEachIndexed { i, it -> assertEquals(it, this[i]) }
    }

    @Test
    fun `testing csv reader`() = readUniverseCsv(universeCsv).run {
        routes.forEachIndexed { i, it -> assertEquals(it, this[i]) }
    }
}