@file:Suppress("unused")

package backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import javax.annotation.PostConstruct



@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class Server(private val context: ApplicationContext) {
    @PostConstruct
    private fun init() = checkProfileLog(context)
}



