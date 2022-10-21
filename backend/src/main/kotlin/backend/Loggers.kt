package backend

import backend.Constants.PROFILE_CLOUD
import backend.Constants.PROFILE_DEVELOPMENT
import backend.Constants.PROFILE_PRODUCTION
import backend.Constants.STARTUP_LOG_MSG_KEY
import backend.Log.log
import org.apache.logging.log4j.LogManager.getLogger
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*

/*=================================================================================*/

object Log {
    @JvmStatic
    val log: Logger by lazy { getLogger(Log.javaClass) }
}
/*=================================================================================*/
fun checkProfileLog(context: ApplicationContext) = context.environment.activeProfiles.run {
    when {
        contains(PROFILE_DEVELOPMENT) &&
                contains(PROFILE_PRODUCTION)
        -> log.error(
            context.getBean<MessageSource>().getMessage(
                STARTUP_LOG_MSG_KEY,
                arrayOf(
                    PROFILE_DEVELOPMENT,
                    PROFILE_PRODUCTION
                ),
                Locale.getDefault()
            )
        )
    }
    when {
        contains(PROFILE_DEVELOPMENT) &&
                contains(PROFILE_CLOUD)
        -> log.error(
            context.getBean<MessageSource>().getMessage(
                STARTUP_LOG_MSG_KEY,
                arrayOf(
                    PROFILE_DEVELOPMENT,
                    PROFILE_CLOUD
                ),
                Locale.getDefault()
            )
        )
    }
}

/*=================================================================================*/
fun bootstrapLog(context: ApplicationContext): Unit =
    log.info(
        bootstrapLogMessage(
            appName = context.environment.getProperty("spring.application.name"),
            protocol = if (context.environment.getProperty("server.ssl.key-store") != null) "https"
            else "http",
            serverPort = context.environment.getProperty("server.port"),
            contextPath = context.environment.getProperty("server.servlet.context-path") ?: "/",
            hostAddress = try {
                InetAddress.getLocalHost().hostAddress
            } catch (e: UnknownHostException) {
                log.warn(
                    "The host name could not be determined, " +
                            "using `localhost` as fallback"
                )
                Constants.DEV_HOST
            },
            profiles = context.environment.activeProfiles.joinToString(separator = ",")
        )
    )
/*=================================================================================*/
private fun bootstrapLogMessage(
    appName: String?,
    protocol: String,
    serverPort: String?,
    contextPath: String,
    hostAddress: String,
    profiles: String
): String = """${"\n\n\n"}
----------------------------------------------------------
go visit https://www.cheroliv.com    
----------------------------------------------------------
Application '${appName}' is running! Access URLs:
Local:      $protocol://localhost:$serverPort$contextPath
External:   $protocol://$hostAddress:$serverPort$contextPath
Profile(s): $profiles
----------------------------------------------------------
${"\n\n\n"}""".trimIndent()

/*=================================================================================*/
