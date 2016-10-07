package com.andrewthom.springsecurityloginlogger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.andrewthom.springsecurityloginlogger.configuration.LoginAttemptLoggingConfiguration;

/**
 * This annotation can be placed on any Spring configuration class to 
 * enable login attempt logging to a JDBC database.  The prerequisite
 * for this to work is that there must be a JdbcTemplate bean defined.
 * Adding this annotation will not automatically create a JdbcTemplate
 * bean.
 * 
 * @author Andrew Thom
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(LoginAttemptLoggingConfiguration.class)
@Configuration
public @interface EnableLoginAttemptLogging {
	/**
	 * Used to define the table name in the database.
	 */
	String tableName() default "login_attempts";
	
	/**
	 * Used to denote whether or not the logging should
	 * run in a separate thread.
	 */
	boolean runInSeparateThread() default true;

	/**
	 * By default, logging of failed attempts is enabled. 
	 * To disable, set this value to false on the annotation.
	 */
	boolean logFailedAttempts() default true;
	
	/**
	 * By default, logging of successful attempts is enabled. 
	 * To disable, set this value to false on the annotation.
	 */
	boolean logSuccessfulAttempts() default true;
}
