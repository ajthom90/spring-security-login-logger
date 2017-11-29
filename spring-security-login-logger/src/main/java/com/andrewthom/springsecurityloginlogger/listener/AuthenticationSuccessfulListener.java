package com.andrewthom.springsecurityloginlogger.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import com.andrewthom.springsecurityloginlogger.service.LoginAttemptService;

import static com.andrewthom.springsecurityloginlogger.listener.AuthenticationResult.SUCCESS;

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
		service.putAttemptInDatabase(userName, SUCCESS);
	}
}
