@file:Suppress("unused")

package backend

import backend.Log.log
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/*=================================================================================*/

/*=================================================================================*/
@Service
@Transactional
class RoadMapService(
  private val routeRepository: RouteRepository
)

@Component
@Profile(Constants.SPRING_PROFILE_CLI)
class OnBoardComputerCliRunner(
  private var context: ApplicationContext,
  private val roadMapService: RoadMapService
) : CommandLineRunner, ApplicationContextAware {
  override fun setApplicationContext(applicationContext: ApplicationContext) {
    context = applicationContext
  }
  override fun run(vararg args: String?) {
    //TODO: computation
    log.info("bean provided by spring container : ${context.beanDefinitionNames.toList()}")
  }

}