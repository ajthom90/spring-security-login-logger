package com.github.ajthom90.springsecurityloginlogger.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import com.github.ajthom90.springsecurityloginlogger.service.LoginAttemptService;

public class FailedAuthenticationListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
	private LoginAttemptService service;
	
	public FailedAuthenticationListener(LoginAttemptService service)
	{
		this.service = service;
	}

	@Override
	public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent event) {
		final String userName = event.getAuthentication().getName();
		service.putAttemptInDatabase(userName, false);
	}
}
