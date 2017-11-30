package com.andrewthom.springsecurityloginlogger.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import com.andrewthom.springsecurityloginlogger.service.LoginAttemptService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
		service.putAttemptInDatabase(userName, SUCCESS, ipAddress);
	}
}
