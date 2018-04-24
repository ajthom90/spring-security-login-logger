package com.andrewthom.springsecurityloginlogger.jdbc.configuration

import com.andrewthom.springsecurityloginlogger.common.configuration.BaseConfiguration
import com.andrewthom.springsecurityloginlogger.jdbc.EnableJdbcLoginAttemptLogging
import com.andrewthom.springsecurityloginlogger.jdbc.configuration.JdbcConstants.DEFAULT_TABLE_NAME
import com.andrewthom.springsecurityloginlogger.jdbc.service.JdbcTemplateLoginAttemptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportAware
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotationMetadata
import org.springframework.jdbc.core.JdbcTemplate
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.sql.DataSource

@Configuration
class JdbcLoginAttemptLoggingConfiguration : BaseConfiguration(), ImportAware {
	private var tableName: String? = null
	private var runInSeparateThread: Boolean = false
	private var databaseType: JdbcDatabaseType = JdbcDatabaseType.MYSQL

	@Bean
	fun loginAttemptService(dataSource: DataSource, @Autowired(required = false) threadPool: ExecutorService?): JdbcTemplateLoginAttemptService {
		val threadPoolToUse = threadPool ?: Executors.newCachedThreadPool()
		return JdbcTemplateLoginAttemptService(JdbcTemplate(dataSource), threadPoolToUse, tableName ?: DEFAULT_TABLE_NAME, runInSeparateThread, databaseType)
	}

	override fun setImportMetadata(importMetadata: AnnotationMetadata) {
		val enableAttrMap = importMetadata.getAnnotationAttributes(EnableJdbcLoginAttemptLogging::class.java.name)
		val enableAttrs = AnnotationAttributes.fromMap(enableAttrMap)
		this.tableName = enableAttrs.getString("tableName")
		this.runInSeparateThread = enableAttrs.getBoolean("runInSeparateThread")
		this.logFailedAttempts = enableAttrs.getBoolean("logFailedAttempts")
		this.logSuccessfulAttempts = enableAttrs.getBoolean("logSuccessfulAttempts")
		this.databaseType = enableAttrs.getEnum("databaseType")
	}
}

object JdbcConstants {
	const val DEFAULT_TABLE_NAME = "login_attempts"
}

enum class JdbcDatabaseType(val insertQuery: String) {
	MYSQL("INSERT INTO %%TABLE_NAME%%(id, username, datetime, result, ip_address) VALUES (?, ?, ?, ?, ?)")
}
