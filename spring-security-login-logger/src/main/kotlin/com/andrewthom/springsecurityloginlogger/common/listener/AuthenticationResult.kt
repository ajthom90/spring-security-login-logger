package com.andrewthom.springsecurityloginlogger.common.listener

import org.springframework.security.authentication.event.*
import kotlin.reflect.KClass

enum class AuthenticationResult(private val klass: KClass<out AbstractAuthenticationEvent>) {
	SUCCESS(AuthenticationSuccessEvent::class),
	FAILURE(AbstractAuthenticationFailureEvent::class),
	BAD_CREDENTIALS(AuthenticationFailureBadCredentialsEvent::class),
	ACCOUNT_CREDENTIALS_EXPIRED(AuthenticationFailureCredentialsExpiredEvent::class),
	ACCOUNT_DISABLED(AuthenticationFailureDisabledEvent::class),
	ACCOUNT_EXPIRED(AuthenticationFailureExpiredEvent::class),
	ACCOUNT_LOCKED(AuthenticationFailureLockedEvent::class),
	PROVIDER_NOT_FOUND(AuthenticationFailureProviderNotFoundEvent::class),
	PROXY_UNTRUSTED(AuthenticationFailureProxyUntrustedEvent::class),
	AUTH_SERVICE_EXCEPTION(AuthenticationFailureServiceExceptionEvent::class);

	companion object {
		private val CLASS_TO_RESULT: Map<KClass<out AbstractAuthenticationEvent>, AuthenticationResult> = values().associateBy { it.klass }

		fun getFailureByClass(klass: KClass<out AbstractAuthenticationFailureEvent>): AuthenticationResult {
			return CLASS_TO_RESULT[klass] ?: FAILURE
		}
	}
}
