package com.wedo.mwohalkka.server.admin.config

import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.reactive.function.client.ExchangeStrategies

import org.springframework.web.reactive.function.client.WebClient

@Configuration
class AppContext {

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper.configuration.isAmbiguityIgnored = false
        return modelMapper
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun getValidation(@Autowired messageSource: MessageSource): LocalValidatorFactoryBean {
        return LocalValidatorFactoryBean().apply {
            setValidationMessageSource(messageSource)
        }
    }

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
            .codecs { configurer: ClientCodecConfigurer ->
                configurer
                    .defaultCodecs()
                    .maxInMemorySize(16 * 1024 * 1024)
            }
            .build())
            .build()
    }
}
