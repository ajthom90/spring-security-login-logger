package com.andrewthom.springsecurityloginlogger.jdbc.configuration

import java.util.concurrent.Executors

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportAware
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import org.springframework.jdbc.core.JdbcTemplate

import com.andrewthom.springsecurityloginlogger.common.listener.AuthenticationSuccessfulListener
import com.andrewthom.springsecurityloginlogger.common.listener.FailedAuthenticationListener
import com.andrewthom.springsecurityloginlogger.jdbc.EnableJdbcLoginAttemptLogging
import com.andrewthom.springsecurityloginlogger.jdbc.service.JdbcTemplateLoginAttemptService
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.ExecutorService

@Configuration
class JdbcLoginAttemptLoggingConfiguration : ImportAware {
	private var tableName: String? = null
	private var runInSeparateThread: Boolean = false
	private var logFailedAttempts: Boolean = false
	private var logSuccessfulAttempts: Boolean = false

	@Bean
	fun loginAttemptService(template: JdbcTemplate, @Autowired(required = false) threadPool: ExecutorService?): JdbcTemplateLoginAttemptService {
		val threadPoolToUse = threadPool ?: Executors.newCachedThreadPool()
		return JdbcTemplateLoginAttemptService(template, threadPoolToUse, tableName ?: DEFAULT_TABLE_NAME, runInSeparateThread)
	}

	@Bean
	fun successfulCondition(service: JdbcTemplateLoginAttemptService): AuthenticationSuccessfulListener {
		return AuthenticationSuccessfulListener(service, logSuccessfulAttempts)
	}

	@Bean
	fun failedCondition(service: JdbcTemplateLoginAttemptService): FailedAuthenticationListener {
		return FailedAuthenticationListener(service, logFailedAttempts)
	}

	override fun setImportMetadata(importMetadata: AnnotationMetadata) {
		val enableAttrMap = importMetadata.getAnnotationAttributes(EnableJdbcLoginAttemptLogging::class.java.name)
		val enableAttrs = AnnotationAttributes.fromMap(enableAttrMap)
		this.tableName = enableAttrs.getString("tableName")
		this.runInSeparateThread = enableAttrs.getBoolean("runInSeparateThread")
		this.logFailedAttempts = enableAttrs.getBoolean("logFailedAttempts")
		this.logSuccessfulAttempts = enableAttrs.getBoolean("logSuccessfulAttempts")
	}

	companion object {
		private const val DEFAULT_TABLE_NAME = "login_attempts"
	}
}
