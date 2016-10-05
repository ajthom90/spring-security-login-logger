package com.github.ajthom90.springsecurityloginlogger.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import com.github.ajthom90.springsecurityloginlogger.service.LoginAttemptService;

public class AuthenticationSuccessfulListener implements ApplicationListener<AuthenticationSuccessEvent>
{
	private LoginAttemptService service;
	
	public AuthenticationSuccessfulListener(LoginAttemptService service) {
		this.service = service;
	}

	@Override
	public void onApplicationEvent(final AuthenticationSuccessEvent event)
	{
		final String userName = event.getAuthentication().getName();
		service.putAttemptInDatabase(userName, true);
	}
}

