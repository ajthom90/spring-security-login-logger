package com.github.ajthom90.springsecurityloginlogger.configuration;

import java.util.Map;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.ajthom90.springsecurityloginlogger.EnableLoginAttemptLogging;
import com.github.ajthom90.springsecurityloginlogger.listener.AuthenticationSuccessfulListener;
import com.github.ajthom90.springsecurityloginlogger.listener.FailedAuthenticationListener;
import com.github.ajthom90.springsecurityloginlogger.service.LoginAttemptService;

@Configuration
public class LoginAttemptLoggingConfiguration implements ImportAware {

	private String tableName;

	private boolean runInSeparateThread;

	private boolean logFailedAttempts;

	private boolean logSuccessfulAttempts;

	@Bean
	public LoginAttemptService loginAttemptService(JdbcTemplate template) {
		return new LoginAttemptService(template, Executors.newCachedThreadPool(), tableName, runInSeparateThread);
	}

	@Bean
	@Conditional(LogSuccessfulAttemptsCondition.class)
	public AuthenticationSuccessfulListener successfulCondition(LoginAttemptService service) {
		return new AuthenticationSuccessfulListener(service);
	}
	
	@Bean
	@Conditional(LogFailedAttemptsCondition.class)
	public FailedAuthenticationListener failedCondition(LoginAttemptService service) {
		return new FailedAuthenticationListener(service);
	}

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> enableAttrMap = importMetadata
				.getAnnotationAttributes(EnableLoginAttemptLogging.class.getName());
		AnnotationAttributes enableAttrs = AnnotationAttributes.fromMap(enableAttrMap);
		this.tableName = enableAttrs.getString("tableName");
		this.runInSeparateThread = enableAttrs.getBoolean("runInSeparateThread");
		this.logFailedAttempts = enableAttrs.getBoolean("logFailedAttempts");
		this.logSuccessfulAttempts = enableAttrs.getBoolean("logSuccessfulAttempts");
	}

	public class LogFailedAttemptsCondition implements Condition {
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return LoginAttemptLoggingConfiguration.this.logFailedAttempts;
		}
	}

	public class LogSuccessfulAttemptsCondition implements Condition {
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return LoginAttemptLoggingConfiguration.this.logSuccessfulAttempts;
		}
	}
}
