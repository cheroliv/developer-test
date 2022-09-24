@file:Suppress("unused")

package backend

import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import org.zalando.problem.Status.BAD_REQUEST
import java.net.URI

/*=================================================================================*/


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

