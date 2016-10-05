package com.github.ajthom90.springsecurityloginlogger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.ajthom90.springsecurityloginlogger.configuration.LoginAttemptLoggingConfiguration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(LoginAttemptLoggingConfiguration.class)
@Configuration
public @interface EnableLoginAttemptLogging {
	String tableName() default "login_attempts";
	
	boolean runInSeparateThread() default true;

	boolean logFailedAttempts() default true;
	
	boolean logSuccessfulAttempts() default true;
}
