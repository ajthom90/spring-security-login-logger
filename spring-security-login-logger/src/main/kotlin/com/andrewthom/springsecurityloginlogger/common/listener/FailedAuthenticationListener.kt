package com.andrewthom.springsecurityloginlogger.common.listener

import com.andrewthom.springsecurityloginlogger.common.service.LoginAttemptService
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent

import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

class FailedAuthenticationListener(private val service: LoginAttemptService, private val log: Boolean) : ApplicationListener<AbstractAuthenticationFailureEvent> {
	override fun onApplicationEvent(event: AbstractAuthenticationFailureEvent) {
		if (!log) {
			return
		}
		val auth = event.authentication
		val username = if (auth != null) {
			event.authentication.name
		} else {
			"unknown"
		}
		var ipAddress = "unknown"
		val currentRequest = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
		if (currentRequest != null) {
			val ipHeader = currentRequest.getHeader("X-Forwarded-For")
			if (ipHeader != null && !ipHeader.isEmpty()) {
				ipAddress = ipHeader
			}
		}
		if ("unknown" == ipAddress) {
			try {
				ipAddress = (event.authentication.details as WebAuthenticationDetails).remoteAddress
			} catch (e: Exception) {
				// continue without getting IP address
			}
		}
		val result = AuthenticationResult.getFailureByClass(event::class)
		service.putAttemptInDatabase(username, result, ipAddress)
	}
}
