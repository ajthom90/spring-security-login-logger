package com.andrewthom.springsecurityloginlogger.jdbc.service

import com.andrewthom.springsecurityloginlogger.common.listener.AuthenticationResult
import com.andrewthom.springsecurityloginlogger.jdbc.configuration.JdbcConstants
import com.andrewthom.springsecurityloginlogger.jdbc.configuration.JdbcDatabaseType
import io.mockk.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.jdbc.core.JdbcTemplate
import java.util.*
import java.util.concurrent.Executors

class JdbcTemplateLoginAttemptServiceTests {
	lateinit var template: JdbcTemplate

	@Before
	fun setup() {
		val uuid = mockk<UUID>()
		every { uuid.toString() } returns "GUID"

		staticMockk<UUID>().use {
			every { UUID.randomUUID() } returns uuid
		}

		val test = UUID.randomUUID()
		println(test.toString())

		template = mockk()
		every { template.update(any(), any(), any(), any(), any(), any()) } returns 1
	}

	@Test
	@Ignore
	fun testRunInSameThread() {
		val service = JdbcTemplateLoginAttemptService(template, Executors.newSingleThreadExecutor(), JdbcConstants.DEFAULT_TABLE_NAME, false, JdbcDatabaseType.MYSQL)

		service.logAttempt("username", AuthenticationResult.SUCCESS, "222.222.222.222")

		verify(exactly = 1) { template.update("INSERT INTO ${JdbcConstants.DEFAULT_TABLE_NAME}(id, username, datetime, result, ip_address) VALUES (, ?, ?, ?, ?)", "GUID", "username", any(), AuthenticationResult.SUCCESS.name, "222.222.222.222") }
	}
}
