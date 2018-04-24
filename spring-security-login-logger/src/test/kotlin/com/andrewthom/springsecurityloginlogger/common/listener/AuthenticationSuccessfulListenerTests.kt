package com.andrewthom.springsecurityloginlogger.common.listener

import com.andrewthom.springsecurityloginlogger.common.service.LoginAttemptService
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest

class AuthenticationSuccessfulListenerTests {
	lateinit var auth: Authentication
	lateinit var exception: AuthenticationException
	lateinit var request: HttpServletRequest
	lateinit var requestAttributes: ServletRequestAttributes
	lateinit var loginAttemptService: LoginAttemptService
	lateinit var webAuthenticationDetails: WebAuthenticationDetails

	@Before
	fun setup() {
		auth = mockk()

		every {
			auth.name
		} returns "username"

		exception = object : AuthenticationException("bad") {}
		request = mockk()
		requestAttributes = mockk()

		RequestContextHolder.setRequestAttributes(requestAttributes)

		loginAttemptService = mockk()
		every { loginAttemptService.logAttempt(any(), any(), any() ) } just Runs

		webAuthenticationDetails = mockk()
		every { webAuthenticationDetails.remoteAddress } returns "222.222.222.222"
		every { auth.details } returns webAuthenticationDetails
	}

	@Test
	fun testSuccessAuthentication1_Forwarded() {
		every { request.getHeader("X-Forwarded-For") } returns "123.123.123.123"
		every { requestAttributes.request } returns request

		val event = AuthenticationSuccessEvent(auth)

		// create listener
		val listener = AuthenticationSuccessfulListener(loginAttemptService, true)
		listener.onApplicationEvent(event)

		verify(exactly = 1) { loginAttemptService.logAttempt("username", AuthenticationResult.SUCCESS, "123.123.123.123") }
		verify { event.authentication.details wasNot Called }
	}

	@Test
	fun testSuccessAuthentication1_ForwardedEmpty() {
		every { request.getHeader("X-Forwarded-For") } returns EmptyString
		every { requestAttributes.request } returns request

		val event = AuthenticationSuccessEvent(auth)

		// create listener
		val listener = AuthenticationSuccessfulListener(loginAttemptService, true)
		listener.onApplicationEvent(event)

		verify(exactly = 1) { loginAttemptService.logAttempt("username", AuthenticationResult.SUCCESS, "222.222.222.222") }
		verify(exactly = 1) { auth.details }
	}

	@Test
	fun testSuccessAuthentication1_DoNotLog() {
		every { request.getHeader("X-Forwarded-For") } returns EmptyString
		every { requestAttributes.request } returns request

		val event = AuthenticationSuccessEvent(auth)

		// create listener
		val listener = AuthenticationSuccessfulListener(loginAttemptService, false)
		listener.onApplicationEvent(event)

		verify(exactly = 0) { loginAttemptService.logAttempt("username", AuthenticationResult.SUCCESS, "222.222.222.222") }
		verify(exactly = 0) { auth.details }
	}
}