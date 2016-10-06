package com.github.ajthom90.springsecurityloginlogger.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import com.github.ajthom90.springsecurityloginlogger.service.LoginAttemptService;

public class FailedAuthenticationListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
	private LoginAttemptService service;
	private boolean log;

	public FailedAuthenticationListener(LoginAttemptService service, boolean log) {
		this.service = service;
		this.log = log;
	}

	@Override
	public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent event) {
		if (!log) {
			return;
		}
		final String userName = event.getAuthentication().getName();
		service.putAttemptInDatabase(userName, false);
	}
}
