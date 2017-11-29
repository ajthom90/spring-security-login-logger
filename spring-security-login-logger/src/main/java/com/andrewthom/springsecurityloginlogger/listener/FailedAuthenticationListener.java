package com.andrewthom.springsecurityloginlogger.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import com.andrewthom.springsecurityloginlogger.service.LoginAttemptService;
import org.springframework.security.core.Authentication;

public class FailedAuthenticationListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {
	private LoginAttemptService service;
	private boolean log;

	public FailedAuthenticationListener(LoginAttemptService service, boolean log) {
		this.service = service;
		this.log = log;
	}

	@Override
	public void onApplicationEvent(final AbstractAuthenticationFailureEvent event) {
		if (!log) {
			return;
		}
		Authentication auth = event.getAuthentication();
		String username;
		if (auth != null) {
			username = event.getAuthentication().getName();
		}
		else {
			username = "unknown";
		}
		AuthenticationResult result = AuthenticationResult.getFailureByClass(event.getClass());
		service.putAttemptInDatabase(username, result);
	}
}
