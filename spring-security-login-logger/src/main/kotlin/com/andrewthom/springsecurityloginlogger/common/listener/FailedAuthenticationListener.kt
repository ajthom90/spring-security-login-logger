package com.andrewthom.springsecurityloginlogger.common.listener

import com.andrewthom.springsecurityloginlogger.common.service.LoginAttemptService
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent

import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

class FailedAuthenticationListener(private val service: LoginAttemptService, private val log: Boolean,
								   private val failedAuthenticationHandler: FailedAuthenticationHandler) : ApplicationListener<AbstractAuthenticationFailureEvent> {
	override fun onApplicationEvent(event: AbstractAuthenticationFailureEvent) {
		if (!log) {
			return
		}
		val username = event.authentication?.name ?: "unknown"
		var ipAddress = "unknown"
		val currentRequest = (RequestContextHolder.currentRequestAttributes() as? ServletRequestAttributes)?.request
		if (currentRequest != null) {
			val ipHeader = currentRequest.getHeader("X-Forwarded-For")
			if (!ipHeader.isNullOrEmpty()) {
				ipAddress = ipHeader
			}
		}
		if ("unknown" == ipAddress) {
			ipAddress = (event.authentication.details as? WebAuthenticationDetails)?.remoteAddress ?: "unknown"
		}
		val result = AuthenticationResult.getFailureByClass(event::class)
		service.logAttempt(username, result, ipAddress)
		failedAuthenticationHandler.handleFailedAuthentication(username = username, result = result)
	}
}

interface FailedAuthenticationHandler {
	fun handleFailedAuthentication(username: String, result: AuthenticationResult)
}

internal object DefaultFailedAuthenticationHandler: FailedAuthenticationHandler {
	override fun handleFailedAuthentication(username: String, result: AuthenticationResult) {
		// do nothing
	}
}
