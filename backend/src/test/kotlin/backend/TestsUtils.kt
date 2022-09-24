@file:Suppress("unused")

package backend

import backend.Constants.SPRING_PROFILE_CONF_DEFAULT_KEY
import backend.Constants.SPRING_PROFILE_TEST
import org.springframework.boot.SpringApplication

const val BASE_URL_DEV = "http://localhost:8080"

fun testLoader(app: SpringApplication) = with(app) {
    setDefaultProperties(
        hashMapOf<String, Any>().apply {
            set(
                SPRING_PROFILE_CONF_DEFAULT_KEY,
                SPRING_PROFILE_TEST
            )
        })
    setAdditionalProfiles(SPRING_PROFILE_TEST)
}