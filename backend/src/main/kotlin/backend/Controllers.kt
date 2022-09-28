@file:Suppress("unused")

package backend

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


/*=================================================================================*/
@RestController
@RequestMapping("/api/roadmap")
class RoadMapController(private val roadMapService: RoadMapService) {

    @PostMapping("give-me-the-odds")
    @ResponseStatus(HttpStatus.OK)
    suspend fun giveMeOdds(): Int = -1
}
/*=================================================================================*/
