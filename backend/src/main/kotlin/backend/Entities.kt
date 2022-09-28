@file:Suppress("unused")

package backend

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive


/*=================================================================================*/
interface RouteRecord

/*=================================================================================*/
@Table("`routes`")
data class RouteEntity
@JvmOverloads constructor
    (
    @Id var id: UUID? = null,
    @field:NotBlank
    var origin: String,
    @field:NotBlank
    var destination: String,
    @field:Positive
//    @Column("`travel_time`")
    var travel_time: Int,
) : RouteRecord {
    constructor(route: Route) : this(
        origin = route.origin,
        destination = route.destination,
        travel_time = route.travel_time
    )

//    @org.springframework.data.annotation.PersistenceCreator
//    constructor(
//        id: UUID?,
//        origin: String,
//        destination: String,
//        travel_time: Int
//    ) : this(
//        id=id,
//        origin=origin,
//        destination = destination,
//        travel_time=travel_time,
//    )
//    {
//        this.id = id
//        this.origin
//        this.destination
//        this.travel_time
//    }

    val toDomain: Route
        get() = Route(
            origin = origin,
            destination = destination,
            travel_time = travel_time
        )
}
/*=================================================================================*/
