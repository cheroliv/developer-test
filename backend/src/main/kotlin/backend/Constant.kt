@file:Suppress("unused")

package backend

import java.net.URI
import java.net.URI.create


object Constants {


    //SignUpController
    @JvmStatic
    val ALLOWED_ORDERED_PROPERTIES = arrayOf(
        "id",
        "login",
        "firstName",
        "lastName",
        "email",
        "activated",
        "langKey"
    )

    //Spring profiles
    const val SPRING_PROFILE_DEVELOPMENT = "dev"
    const val SPRING_PROFILE_PRODUCTION = "prod"
    const val SPRING_PROFILE_CLOUD = "cloud"
    const val SPRING_PROFILE_CONF_DEFAULT_KEY = "spring.profiles.default"
    const val SPRING_PROFILE_TEST = "test"
    const val SPRING_PROFILE_HEROKU = "heroku"
    const val SPRING_PROFILE_AWS_ECS = "aws-ecs"
    const val SPRING_PROFILE_AZURE = "azure"
    const val SPRING_PROFILE_SWAGGER = "swagger"
    const val SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase"
    const val SPRING_PROFILE_K8S = "k8s"
    const val SPRING_PROFILE_CLI = "cli"
    val SPRING_PROFILE_CLI_PROPS = mutableMapOf<String, Any>("spring.main.web-application-type" to "none")

    //Config
    const val DEV_HOST = "localhost"

    //HTTP param
    const val REQUEST_PARAM_LANG = "lang"
    const val CONTENT_SECURITY_POLICY =
        "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:"
    const val FEATURE_POLICY =
        "geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'"


    //REST API
    //URIs

    //properties
    const val PROP_ITEM = "backend.item"
    const val PROP_MESSAGE = "backend.message"
    const val PROP_DATABASE_POPULATOR_PATH = "backend.database.populator-path"
    const val STARTUP_LOG_MSG_KEY = "startup.log.msg"


    const val DEFAULT_LANGUAGE = "en"
    const val ERR_CONCURRENCY_FAILURE: String = "error.concurrencyFailure"
    const val ERR_VALIDATION: String = "error.validation"
    private const val PROBLEM_BASE_URL: String = "https://www.cheroliv.com/problem"


    @JvmField
    val DEFAULT_TYPE: URI = create("$PROBLEM_BASE_URL/problem-with-message")

    @JvmField
    val CONSTRAINT_VIOLATION_TYPE: URI = create("$PROBLEM_BASE_URL/constraint-violation")

    @JvmField
    val INVALID_PASSWORD_TYPE: URI = create("$PROBLEM_BASE_URL/invalid-password")

    @JvmField
    val EMAIL_ALREADY_USED_TYPE: URI = create("$PROBLEM_BASE_URL/email-already-used")

    @JvmField
    val LOGIN_ALREADY_USED_TYPE: URI = create("$PROBLEM_BASE_URL/login-already-used")
}