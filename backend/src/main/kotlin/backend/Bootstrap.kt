package backend

import backend.Log.log
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import java.net.InetAddress.getLocalHost
import java.net.UnknownHostException
import java.util.*


/*=================================================================================*/

fun main(args: Array<String>) = runApplication<Server>(*args) {
    with(this) {
        setDefaultProperties(hashMapOf<String, Any>(Constants.SPRING_PROFILE_CONF_DEFAULT_KEY to Constants.SPRING_PROFILE_DEVELOPMENT))
        setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)
    }
}.run { startupLog(context = this) }


/*=================================================================================*/

internal fun checkProfileLog(context: ApplicationContext) =
    context.environment.activeProfiles.run {
        when {
            contains(element = Constants.SPRING_PROFILE_DEVELOPMENT) &&
                    contains(element = Constants.SPRING_PROFILE_PRODUCTION)
            -> log.error(
                context.getBean<MessageSource>().getMessage(
                    Constants.STARTUP_LOG_MSG_KEY,
                    arrayOf(
                        Constants.SPRING_PROFILE_DEVELOPMENT,
                        Constants.SPRING_PROFILE_PRODUCTION
                    ),
                    Locale.getDefault()
                )
            )
        }
        when {
            contains(Constants.SPRING_PROFILE_DEVELOPMENT) &&
                    contains(Constants.SPRING_PROFILE_CLOUD)
            -> log.error(
                context.getBean<MessageSource>().getMessage(
                    Constants.STARTUP_LOG_MSG_KEY,
                    arrayOf(
                        Constants.SPRING_PROFILE_DEVELOPMENT,
                        Constants.SPRING_PROFILE_CLOUD
                    ),
                    Locale.getDefault()
                )
            )
        }
    }


/*=================================================================================*/

private fun startupLogMessage(
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

private fun startupLog(context: ApplicationContext): Unit =
    log.info(
        startupLogMessage(
            appName = context.environment.getProperty("spring.application.name"),
            protocol = if (context.environment.getProperty("server.ssl.key-store") != null) "https"
            else "http",
            serverPort = context.environment.getProperty("server.port"),
            contextPath = context.environment.getProperty("server.servlet.context-path") ?: "/",
            hostAddress = try {
                getLocalHost().hostAddress
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


