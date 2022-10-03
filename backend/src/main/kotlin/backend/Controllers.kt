@file:Suppress("unused")

package backend

import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.io.IOException


/*=================================================================================*/
@RestController
@RequestMapping("/api/roadmap")
class RoadMapController(private val roadMapService: RoadMapService) {

    @PostMapping(
        value = ["give-me-the-odds"],
        consumes = [MULTIPART_FORM_DATA_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(OK)
    @Throws(IOException::class)
    suspend fun giveMeOdds(
        @RequestPart("empire") name: String,
        @RequestPart("empire") filePart: FilePart
    ): ResponseEntity<Int> = ResponseEntity<Int>(-1, OK)
}
/*=================================================================================*/
