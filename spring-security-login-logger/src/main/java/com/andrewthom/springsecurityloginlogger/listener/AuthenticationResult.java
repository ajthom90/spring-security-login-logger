package com.andrewthom.springsecurityloginlogger.listener;

import org.springframework.security.authentication.event.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum AuthenticationResult {
	SUCCESS(AuthenticationSuccessEvent.class),
	FAILURE(AbstractAuthenticationFailureEvent.class),
	BAD_CREDENTIALS(AuthenticationFailureBadCredentialsEvent.class),
	ACCOUNT_CREDENTIALS_EXPIRED(AuthenticationFailureCredentialsExpiredEvent.class),
	ACCOUNT_DISABLED(AuthenticationFailureDisabledEvent.class),
	ACCOUNT_EXPIRED(AuthenticationFailureExpiredEvent.class),
	ACCOUNT_LOCKED(AuthenticationFailureLockedEvent.class),
	PROVIDER_NOT_FOUND(AuthenticationFailureProviderNotFoundEvent.class),
	PROXY_UNTRUSTED(AuthenticationFailureProxyUntrustedEvent.class),
	AUTH_SERVICE_EXCEPTION(AuthenticationFailureServiceExceptionEvent.class);

	private static final Map<Class<? extends AbstractAuthenticationEvent>, AuthenticationResult> CLASS_TO_RESULT;
	static {
		Map<Class<? extends AbstractAuthenticationEvent>, AuthenticationResult> builder = new HashMap<>();
		for (AuthenticationResult result : values()) {
			builder.put(result.klass, result);
		}
		CLASS_TO_RESULT = Collections.unmodifiableMap(builder);
	}

	private final Class<? extends AbstractAuthenticationEvent> klass;

	AuthenticationResult(Class<? extends AbstractAuthenticationEvent> klass) {
		this.klass = klass;
	}

	public static AuthenticationResult getFailureByClass(Class<? extends AbstractAuthenticationFailureEvent> klass) {
		return CLASS_TO_RESULT.getOrDefault(klass, FAILURE);
	}
}
