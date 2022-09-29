@file:Suppress("unused")

package backend

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.charset.StandardCharsets.UTF_8


/*=================================================================================*/
@RestController
@RequestMapping("/api/roadmap")
class RoadMapController(private val roadMapService: RoadMapService) {

    @PostMapping("give-me-the-odds")
    @ResponseStatus(HttpStatus.OK)
    suspend fun giveMeOdds(
        @RequestParam("empire") empire: MultipartFile
    ): ResponseEntity<Int> {
        println(empire.resource.file.readText(UTF_8))
        return ResponseEntity<Int>(-1, HttpStatus.OK)
    }
}
/*=================================================================================*/
