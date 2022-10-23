@file:Suppress("unused")

package backend

import java.net.URI
import java.net.URI.create


object Constants {
    const val FIELD_ERRORS_KEY = "fieldErrors"
    const val MESSAGE_KEY = "message"
    const val PATH_KEY = "path"
    const val VIOLATIONS_KEY = "violations"

    const val CSV_DELIMITER = ";"
    const val LUCKY = 1.0
    const val UNLUCKY = 0.0
    const val REFUEL_DURATION = 1
    const val NORMAL_TERMINATION = 0

    //Spring profiles
    const val PROFILE_DEVELOPMENT = "dev"
    const val PROFILE_PRODUCTION = "prod"
    const val PROFILE_CLOUD = "cloud"
    const val PROFILE_CONF_DEFAULT_KEY = "spring.profiles.default"
    const val PROFILE_TEST = "test"
    const val PROFILE_GCLOUD = "gcloud"
    const val PROFILE_K8S = "k8s"
    const val PROFILE_CLI = "cli"
    val PROFILE_CLI_PROPS = mapOf("spring.main.web-application-type" to "none")


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