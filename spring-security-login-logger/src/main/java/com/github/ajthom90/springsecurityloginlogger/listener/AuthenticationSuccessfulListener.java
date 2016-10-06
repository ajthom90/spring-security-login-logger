package com.github.ajthom90.springsecurityloginlogger.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import com.github.ajthom90.springsecurityloginlogger.service.LoginAttemptService;

public class AuthenticationSuccessfulListener implements ApplicationListener<AuthenticationSuccessEvent> {
	private LoginAttemptService service;

	private boolean log;

	public AuthenticationSuccessfulListener(LoginAttemptService service, boolean log) {
		this.service = service;
		this.log = log;
	}

	@Override
	public void onApplicationEvent(final AuthenticationSuccessEvent event) {
		if (!log) {
			return;
		}
		final String userName = event.getAuthentication().getName();
		service.putAttemptInDatabase(userName, true);
	}
}
