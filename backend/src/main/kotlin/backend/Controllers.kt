@file:Suppress("unused")

package backend

import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


/*=================================================================================*/
@RestController
@RequestMapping("/api")
class RoadMapController(private val roadMapService: RoadMapService) {

    @PostMapping("give-me-the-odds")
    @ResponseStatus(OK)
    suspend fun giveMeOdds(
        @RequestBody @Valid empire: Empire
    ): ResponseEntity<Double> = ResponseEntity(
        roadMapService.giveMeTheOdds(empire),
        OK
    )
}
/*=================================================================================*/
