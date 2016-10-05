package com.github.ajthom90.springsecurityloginlogger.service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class LoginAttemptService {
	@Autowired
	private JdbcTemplate template;

	@Autowired
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

	private static final String SQL = "INSERT INTO %%TABLE_NAME%% VALUES (?, ?, ?, ?)"; //$NON-NLS-1$

	/**
	 * This service will log login attempts to a database. This is handled in
	 * another thread, so the
	 * 
	 * @param username
	 * @param success
	 */
	public void putAttemptInDatabase(final String username, final boolean success) {
		if (runInSeparateThread) {
			threadPool.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					final String guid = "J" + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
					final Date datetime = new Date();
					template.update(getQuery(), guid, username, datetime, success);
					return null;
				}
			});
		}
		else
		{
			final String guid = "J" + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
			final Date datetime = new Date();
			template.update(getQuery(), guid, username, datetime, success);
		}
	}

	private String getQuery() {
		return SQL.replace("%%TABLE_NAME%%", tableName);
	}
}
