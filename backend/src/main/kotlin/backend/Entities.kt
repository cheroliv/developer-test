package backend

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive


/*=================================================================================*/
@Table("`ROUTES`")
data class RouteEntity(
    @Id var id: UUID? = null,
    @field:NotBlank
    @Column("ORIGIN")
    var origin: String,
    @field:NotBlank
    @Column("DESTINATION")
    var destination: String,
    @field:Positive
    @Column("`TRAVEL_TIME`")
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
