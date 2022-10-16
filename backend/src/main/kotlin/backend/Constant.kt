@file:Suppress("unused")

package backend

import java.net.URI
import java.net.URI.create


object Constants {



    const val REFUEL_DURATION = 1
    const val NORMAL_TERMINATION = 0

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
}