@file:Suppress("unused")

package backend

import backend.Log.log
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.IOException


/*=================================================================================*/
@RestController
@RequestMapping("/api")
class RoadMapController(private val roadMapService: RoadMapService) {

    @PostMapping("give-me-the-odds")
    @ResponseStatus(OK)
    @Throws(IOException::class)
    suspend fun giveMeOdds(
        @RequestPart("empire") empire: String
    ): ResponseEntity<Double> = ResponseEntity(
        roadMapService.giveMeTheOdds(empire), OK)
        .also { log.info("empire: $empire") }
}
/*=================================================================================*/
