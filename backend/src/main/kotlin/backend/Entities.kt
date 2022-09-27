@file:Suppress("unused")

package backend

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive


/*=================================================================================*/
interface RouteRecord

/*=================================================================================*/
@Table("`routes`")
data class RouteEntity(
    @Id var id: UUID?,
    @field:NotBlank
    val origin: String,
    @field:NotBlank
    val destination: String,
    @field:Positive
    @Column("`travel_time`")
    val travelTime: Int,
) : RouteRecord
/*=================================================================================*/
