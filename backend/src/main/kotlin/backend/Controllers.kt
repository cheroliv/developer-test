@file:Suppress("unused")

package backend

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/*=================================================================================*/
@RestController
@RequestMapping("/api/roadmap")
class RoadMapController(private val roadMapService: RoadMapService)
/*=================================================================================*/
