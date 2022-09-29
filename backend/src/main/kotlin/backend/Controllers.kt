@file:Suppress("unused")

package backend

import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import kotlin.jvm.Throws


/*=================================================================================*/
@RestController
@RequestMapping("/api/roadmap")
class RoadMapController(private val roadMapService: RoadMapService) {


    @PostMapping(
        value = ["give-me-the-odds"],
//        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    @ResponseStatus(OK)
    @Throws(IOException::class)
    suspend fun giveMeOdds(
//        @RequestPart("empire") empire: MultipartFile
    ): ResponseEntity<Int> {
//        println(empire.resource.file.readText(StandardCharsets.UTF_8))
        return ResponseEntity<Int>(-1, OK)
    }
}
/*=================================================================================*/
