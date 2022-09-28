@file:Suppress("unused")

package backend

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive


/*=================================================================================*/
@Table("`routes`")
data class RouteEntity(
    @Id var id: UUID? = null,
    @field:NotBlank
    var origin: String,
    @field:NotBlank
    var destination: String,
    @field:Positive
    @Column("`travel_time`")
    var travelTime: Int,
) {
    constructor(route: Route) : this(
        origin = route.origin,
        destination = route.destination,
        travelTime = route.travelTime
    )

    val toDomain: Route
        get() = Route(
            origin = origin,
            destination = destination,
            travelTime = travelTime
        )
}

/*=================================================================================*/
