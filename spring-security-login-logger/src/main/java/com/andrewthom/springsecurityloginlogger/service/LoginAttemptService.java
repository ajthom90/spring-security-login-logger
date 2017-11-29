package com.andrewthom.springsecurityloginlogger.service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.andrewthom.springsecurityloginlogger.listener.AuthenticationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class LoginAttemptService {
	private JdbcTemplate template;
	private ExecutorService threadPool;
	private String tableName;
	private boolean runInSeparateThread;

	public LoginAttemptService(JdbcTemplate template, ExecutorService threadPool, String tableName,
			boolean runInSeparateThread) {
		this.template = template;
		this.threadPool = threadPool;
		this.tableName = tableName;
		this.runInSeparateThread = runInSeparateThread;
	}

	private static final String SQL = "INSERT INTO %%TABLE_NAME%%(id, username, datetime, result, ip_address) VALUES (?, ?, ?, ?, ?)"; //$NON-NLS-1$

	/**
	 * This service will log login attempts to a database. This is conditionally handled in a separate thread
	 * based on the annotation setting.
	 * 
	 * @param username The username given in the login attempt
	 * @param result Result of the authentication attempt
	 */
	public void putAttemptInDatabase(final String username, final AuthenticationResult result, String ipAddress) {
		if (runInSeparateThread) {
			threadPool.submit((Callable<Void>) () -> {
				final String guid = "J" + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
				final Date datetime = new Date();
				template.update(getQuery(), guid, username, datetime, result.name(), ipAddress);
				return null;
			});
		}
		else
		{
			final String guid = "J" + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
			final Date datetime = new Date();
			template.update(getQuery(), guid, username, datetime, result.name(), ipAddress);
		}
	}

	private String getQuery() {
		return SQL.replace("%%TABLE_NAME%%", tableName);
	}
}
