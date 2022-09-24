package backend

import org.apache.logging.log4j.LogManager.getLogger
import org.apache.logging.log4j.Logger

object Log {
    @JvmStatic
    val log: Logger by lazy { getLogger(Log.javaClass) }
}