package com.andrewthom.springsecurityloginlogger.jdbc.service

import com.andrewthom.springsecurityloginlogger.common.listener.AuthenticationResult
import com.andrewthom.springsecurityloginlogger.common.service.LoginAttemptService
import org.springframework.jdbc.core.JdbcTemplate

import java.util.Date
import java.util.UUID
import java.util.concurrent.ExecutorService

/**
 * This class is used to take the result from the listeners and add a record of it to the database
 */
class JdbcTemplateLoginAttemptService(private val template: JdbcTemplate, private val threadPool: ExecutorService, private val tableName: String,
									  private val runInSeparateThread: Boolean) : LoginAttemptService {
	private val query: String
		get() = SQL.replace("%%TABLE_NAME%%", tableName)

	/**
	 * This service will log login attempts to a database. This is conditionally handled in a separate thread
	 * based on the annotation setting.
	 *
	 * @param username The username given in the login attempt
	 * @param result Result of the authentication attempt
	 */
	override fun putAttemptInDatabase(username: String, result: AuthenticationResult, ipAddress: String) {
		if (runInSeparateThread) {
			threadPool.submit {
				val guid = "J" + UUID.randomUUID().toString().replace("-".toRegex(), "").toUpperCase()
				val datetime = Date()
				template.update(query, guid, username, datetime, result.name, ipAddress)
			}
		} else {
			val guid = "J" + UUID.randomUUID().toString().replace("-".toRegex(), "").toUpperCase()
			val datetime = Date()
			template.update(query, guid, username, datetime, result.name, ipAddress)
		}
	}

	companion object {
		private const val SQL = "INSERT INTO %%TABLE_NAME%%(id, username, datetime, result, ip_address) VALUES (?, ?, ?, ?, ?)" //$NON-NLS-1$
	}
}
