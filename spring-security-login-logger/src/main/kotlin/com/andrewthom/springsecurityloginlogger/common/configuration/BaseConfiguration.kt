package com.andrewthom.springsecurityloginlogger.common.configuration

import com.andrewthom.springsecurityloginlogger.common.listener.AuthenticationSuccessfulListener
import com.andrewthom.springsecurityloginlogger.common.listener.DefaultFailedAuthenticationHandler
import com.andrewthom.springsecurityloginlogger.common.listener.FailedAuthenticationHandler
import com.andrewthom.springsecurityloginlogger.common.listener.FailedAuthenticationListener
import com.andrewthom.springsecurityloginlogger.jdbc.service.JdbcTemplateLoginAttemptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
abstract class BaseConfiguration {
	var logSuccessfulAttempts: Boolean = false
	var logFailedAttempts: Boolean = false

	@Bean
	fun successfulConditionListener(service: JdbcTemplateLoginAttemptService): AuthenticationSuccessfulListener {
		return AuthenticationSuccessfulListener(service, logSuccessfulAttempts)
	}

	@Bean
	fun failedConditionListener(service: JdbcTemplateLoginAttemptService, @Autowired(required = false) failedAuthenticationHandler: FailedAuthenticationHandler?): FailedAuthenticationListener {
		val handler = failedAuthenticationHandler ?: DefaultFailedAuthenticationHandler
		return FailedAuthenticationListener(service, logFailedAttempts, handler)
	}
}
