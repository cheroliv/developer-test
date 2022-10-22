@file:Suppress("unused")

package backend


import backend.Constants.REQUEST_PARAM_LANG
import backend.Log.log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.i18n.LocaleContext
import org.springframework.context.i18n.SimpleLocaleContext
import org.springframework.core.annotation.Order
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver
import org.springframework.data.web.ReactiveSortHandlerMethodArgumentResolver
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import org.springframework.web.server.i18n.LocaleContextResolver
import org.zalando.problem.jackson.ProblemModule
import org.zalando.problem.spring.webflux.advice.ProblemExceptionHandler
import org.zalando.problem.spring.webflux.advice.ProblemHandling
import org.zalando.problem.violations.ConstraintViolationProblemModule
import reactor.core.publisher.Hooks
import java.util.Locale.forLanguageTag
import java.util.Locale.getDefault

/*=================================================================================*/
@Configuration
@EnableWebFlux
class WebConfiguration(
    private val properties: ApplicationProperties,
) : WebFluxConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        DateTimeFormatterRegistrar().apply {
            setUseIsoFormat(true)
            registerFormatters(registry)
        }
    }

    @Bean
    fun validator(): Validator = LocalValidatorFactoryBean()

    @Bean
    fun javaTimeModule() = JavaTimeModule()

    @Bean
    fun jdk8TimeModule() = Jdk8Module()

    /**
     * The handler must have precedence over
     * WebFluxResponseStatusExceptionHandler
     * and Spring Boot's ErrorWebExceptionHandler
     */
    @Bean
    @Order(-2)
    fun problemHandler(
        mapper: ObjectMapper,
        problemHandling: ProblemHandling
    ): WebExceptionHandler = ProblemExceptionHandler(mapper, problemHandling)

    @Bean
    fun problemModule() = ProblemModule()

    @Bean
    fun constraintViolationProblemModule() = ConstraintViolationProblemModule()

    @Profile("!${Constants.PROFILE_PRODUCTION}")
    fun reactorConfiguration() = Hooks.onOperatorDebug()

    @Bean
    fun corsFilter(): CorsWebFilter = CorsWebFilter(UrlBasedCorsConfigurationSource().apply source@{
        properties.cors.apply config@{
            if (
                allowedOrigins != null &&
                allowedOrigins!!.isNotEmpty()
            ) {
                log.debug("Registering CORS filter")
                registerCorsConfiguration("/api/**", this)
            }
        }
    })

    // TODO: remove when this is supported in spring-data / spring-boot
    @Bean
    fun reactivePageableHandlerMethodArgumentResolver() = ReactivePageableHandlerMethodArgumentResolver()

    // TODO: remove when this is supported in spring-boot
    @Bean
    fun reactiveSortHandlerMethodArgumentResolver() = ReactiveSortHandlerMethodArgumentResolver()

}


/*=================================================================================*/
@Configuration
class LocaleSupportConfiguration : DelegatingWebFluxConfiguration() {

    override fun createLocaleContextResolver(): LocaleContextResolver =
        RequestParamLocaleContextResolver()

    class RequestParamLocaleContextResolver : LocaleContextResolver {
        override fun resolveLocaleContext(exchange: ServerWebExchange): LocaleContext {
            var targetLocale = getDefault()
            val referLang = exchange.request.queryParams[REQUEST_PARAM_LANG]
            if (referLang != null && referLang.isNotEmpty())
                targetLocale = forLanguageTag(referLang[0])
            return SimpleLocaleContext(targetLocale)
        }

        @Throws(UnsupportedOperationException::class)
        override fun setLocaleContext(
            exchange: ServerWebExchange,
            localeContext: LocaleContext?
        ): Unit = throw UnsupportedOperationException("Not Supported")
    }
}


/*=================================================================================*/
