@file:Suppress("unused")

package backend

import org.springframework.core.env.Environment
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.dao.DataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import org.zalando.problem.*
import org.zalando.problem.Problem.builder
import org.zalando.problem.Status.BAD_REQUEST
import org.zalando.problem.spring.webflux.advice.ProblemHandling
import org.zalando.problem.violations.ConstraintViolationProblem
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.just
import java.io.Serializable
import java.net.URI
import org.zalando.problem.Problem.DEFAULT_TYPE as PROBLEM_DEFAULT_TYPE

/*=================================================================================*/

open class AlertProblem(
    type: URI,
    defaultMessage: String,
    val entityName: String,
    val errorKey: String
) : AbstractThrowableProblem(
    type,
    defaultMessage,
    BAD_REQUEST,
    null,
    null,
    null,
    getAlertParameters(entityName, errorKey)
) {
    constructor(
        defaultMessage: String,
        entityName: String,
        errorKey: String
    ) : this(DEFAULT_TYPE, defaultMessage, entityName, errorKey)

    override fun getCause(): Exceptional? = super.cause

    companion object {

        private const val serialVersionUID = 1L

        private fun getAlertParameters(
            entityName: String,
            errorKey: String
        ): MutableMap<String, Any> =
            mutableMapOf(
                "message" to "error.$errorKey",
                "params" to entityName
            )
    }
}

/*=================================================================================*/

/**
 * Controller advice to translate the backend side problems to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@Component
@ControllerAdvice
class ProblemTranslator(
    private val env: Environment,
    private val properties: ApplicationProperties
) : ProblemHandling {

    companion object {
        private const val FIELD_ERRORS_KEY = "fieldErrors"
        private const val MESSAGE_KEY = "message"
        private const val PATH_KEY = "path"
        private const val VIOLATIONS_KEY = "violations"
    }

    class FieldErrorVM(
        val objectName: String,
        val field: String,
        val message: String?
    ) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed.
     */
    override fun process(
        entity: ResponseEntity<Problem>,
        request: ServerWebExchange
    ): Mono<ResponseEntity<Problem>> {
        @Suppress("SENSELESS_COMPARISON")
        if (entity == null) return Mono.empty()
        val problem = entity.body
        if (
            !(problem is ConstraintViolationProblem
                    || problem is DefaultProblem)
        ) return just(entity)
        val builder = builder()
            .withType(
                if (PROBLEM_DEFAULT_TYPE == problem.type) Constants.DEFAULT_TYPE
                else problem.type
            )
            .withStatus(problem.status)
            .withTitle(problem.title)
            .with(PATH_KEY, request.request.path.value())

        if (problem is ConstraintViolationProblem) builder
            .with(VIOLATIONS_KEY, problem.violations)
            .with(MESSAGE_KEY, Constants.ERR_VALIDATION)
        else {
            builder
                .withCause((problem as DefaultProblem).cause)
                .withDetail(problem.detail)
                .withInstance(problem.instance)
            problem.parameters.forEach { (key, value) -> builder.with(key, value) }
            if (!problem.parameters.containsKey(MESSAGE_KEY) && problem.status != null) {
                builder.with(
                    MESSAGE_KEY,
                    "error.http." + problem.status!!.statusCode
                )
            }
        }
        return just(
            ResponseEntity<Problem>(
                builder.build(),
                entity.headers,
                entity.statusCode
            )
        )
    }

    override fun handleBindingResult(
        ex: WebExchangeBindException, request: ServerWebExchange
    ): Mono<ResponseEntity<Problem>> = create(
        ex, builder()
            .withType(Constants.CONSTRAINT_VIOLATION_TYPE)
            .withTitle("Data binding and validation failure")
            .withStatus(BAD_REQUEST)
            .with(MESSAGE_KEY, Constants.ERR_VALIDATION)
            .with(FIELD_ERRORS_KEY, ex.bindingResult.fieldErrors.map {
                FieldErrorVM(
                    it.objectName.replaceFirst(
                        Regex(pattern = "DTO$"),
                        replacement = ""
                    ), it.field, it.code
                )
            }).build(), request
    )


    @ExceptionHandler
    fun handleBadRequestAlertException(
        ex: AlertProblem,
        request: ServerWebExchange
    ): Mono<ResponseEntity<Problem>> =
        create(
            ex, request,
            HttpHeaderUtil.createFailureAlert(
                applicationName = properties.clientApp.name,
                enableTranslation = true,
                entityName = ex.entityName,
                errorKey = ex.errorKey,
                defaultMessage = ex.message
            )
        )

    @ExceptionHandler
    fun handleConcurrencyFailure(
        ex: ConcurrencyFailureException,
        request: ServerWebExchange
    ): Mono<ResponseEntity<Problem>> = create(
        ex, builder()
            .withStatus(Status.CONFLICT)
            .with(MESSAGE_KEY, Constants.ERR_CONCURRENCY_FAILURE)
            .build(), request
    )

    override fun prepare(
        throwable: Throwable,
        status: StatusType,
        type: URI
    ): ProblemBuilder {
        var detail = throwable.message
        if (env.activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
            detail = when (throwable) {
                is HttpMessageConversionException -> "Unable to convert http message"
                is DataAccessException -> "Failure during data access"
                else -> {
                    if (containsPackageName(throwable.message))
                        "Unexpected runtime exceptions"
                    else throwable.message
                }
            }
        }
        return builder()
            .withType(type)
            .withTitle(status.reasonPhrase)
            .withStatus(status)
            .withDetail(detail)
            .withCause(throwable.cause.takeIf { isCausalChainsEnabled }?.let { toProblem(it) })
    }

    private fun containsPackageName(message: String?) =
        listOf(
            "org.",
            "java.",
            "net.",
            "javax.",
            "com.",
            "io.",
            "de.",
            "backend"
        ).any { it == message }


}
/*=================================================================================*/