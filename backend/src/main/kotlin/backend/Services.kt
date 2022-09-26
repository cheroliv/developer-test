@file:Suppress("unused")

package backend

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/*=================================================================================*/

/*=================================================================================*/
@Service
@Transactional
class RoadMapService(
  private val routeRepository: RouteRepository
)