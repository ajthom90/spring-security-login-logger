package com.andrewthom.springsecurityloginlogger.common.service

import com.andrewthom.springsecurityloginlogger.common.listener.AuthenticationResult

interface LoginAttemptService {
	/**
	 * This service will log login attempts to a database. This is conditionally handled in a separate thread
	 * based on the annotation setting.
	 *
	 * @param username The username given in the login attempt
	 * @param result Result of the authentication attempt
	 */
	fun logAttempt(username: String, result: AuthenticationResult, ipAddress: String)
}
