package com.andrewthom.springsecurityloginlogger.jdbc

import com.andrewthom.springsecurityloginlogger.jdbc.configuration.JdbcLoginAttemptLoggingConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * This annotation can be placed on any Spring configuration class to
 * enable login attempt logging to a JDBC database.  The prerequisite
 * for this to work is that there must be a JdbcTemplate bean defined.
 * Adding this annotation will not automatically create a JdbcTemplate
 * bean.
 *
 * @author Andrew Thom
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@MustBeDocumented
@Import(JdbcLoginAttemptLoggingConfiguration::class)
@Configuration
annotation class EnableJdbcLoginAttemptLogging(
		/**
		 * Used to define the table name in the database.
		 */
		val tableName: String = "login_attempts",
		/**
		 * Used to denote whether or not the logging should
		 * run in a separate thread.
		 */
		val runInSeparateThread: Boolean = true,
		/**
		 * By default, logging of failed attempts is enabled.
		 * To disable, set this value to false on the annotation.
		 */
		val logFailedAttempts: Boolean = true,
		/**
		 * By default, logging of successful attempts is enabled.
		 * To disable, set this value to false on the annotation.
		 */
		val logSuccessfulAttempts: Boolean = true)
