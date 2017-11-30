package com.andrewthom.springsecurityloginlogger.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import com.andrewthom.springsecurityloginlogger.service.LoginAttemptService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
		String ipAddress = "unknown";
		HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		if (currentRequest != null) {
			String ipHeader = currentRequest.getHeader("X-Forwarded-For");
			if (ipHeader != null && !ipHeader.isEmpty()) {
				ipAddress = ipHeader;
			}
		}
		if ("unknown".equals(ipAddress)) {
			try {
				ipAddress = ((WebAuthenticationDetails) event.getAuthentication().getDetails()).getRemoteAddress();
			} catch (Exception e) {
				// continue without getting IP address
			}
		}
		AuthenticationResult result = AuthenticationResult.getFailureByClass(event.getClass());
		service.putAttemptInDatabase(username, result, ipAddress);
	}
}
