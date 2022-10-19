@file:Suppress("unused")

package backend

import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.IOException
import javax.validation.Valid


/*=================================================================================*/
@RestController
@RequestMapping("/api")
class RoadMapController(private val roadMapService: RoadMapService) {

    @PostMapping("give-me-the-odds")
    @ResponseStatus(OK)
    @Throws(IOException::class)
    suspend fun giveMeOdds(
        @RequestBody @Valid empire: Empire
    ): ResponseEntity<Double> = ResponseEntity(
        roadMapService.giveMeTheOdds(empire),
        OK
    )
}
/*=================================================================================*/
