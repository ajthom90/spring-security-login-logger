package com.andrewthom.springsecurityloginlogger.common.listener

import com.andrewthom.springsecurityloginlogger.common.listener.AuthenticationResult.SUCCESS
import com.andrewthom.springsecurityloginlogger.common.service.LoginAttemptService
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

class AuthenticationSuccessfulListener(private val service: LoginAttemptService, private val log: Boolean) : ApplicationListener<AuthenticationSuccessEvent> {
	override fun onApplicationEvent(event: AuthenticationSuccessEvent) {
		if (!log) {
			return
		}
		val userName = event.authentication.name
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
		service.logAttempt(userName, SUCCESS, ipAddress)
	}
}
