@file:Suppress("unused")

package backend

import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
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
        @RequestPart("empire") name: String,
        @RequestPart("empire") filePart: FilePart
    ): ResponseEntity<Int> {
        return ResponseEntity<Int>(-1, OK)
    }
}
/*=================================================================================*/
