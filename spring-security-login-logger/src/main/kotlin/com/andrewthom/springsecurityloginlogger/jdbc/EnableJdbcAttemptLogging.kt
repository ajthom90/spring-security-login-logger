package com.andrewthom.springsecurityloginlogger.jdbc

import com.andrewthom.springsecurityloginlogger.jdbc.configuration.JdbcConstants
import com.andrewthom.springsecurityloginlogger.jdbc.configuration.JdbcDatabaseType
import com.andrewthom.springsecurityloginlogger.jdbc.configuration.JdbcLoginAttemptLoggingConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * This annotation can be placed on any Spring configuration class to
 * enable login attempt logging to a JDBC database.  The prerequisite
 * for this to work is that there <strong>must</strong> be a [javax.sql.DataSource] bean
 * defined and configured to access your JDBC database. Adding this
 * annotation will not automatically create a DataSource bean.
 *
 * Also, if [runInSeparateThread] is true, the service will attempt
 * to autowire in an [java.util.concurrent.ExecutorService] bean.
 * If an ExecutorService bean is not present, an ExecutorService
 * will be created for use by calling [java.util.concurrent.Executors.newCachedThreadPool].
 * The ExecutorService created here will not be exposed as a bean,
 * so it is recommended to expose one for this library to use.
 *
 * @author Andrew Thom
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Import(JdbcLoginAttemptLoggingConfiguration::class)
@Configuration
annotation class EnableJdbcLoginAttemptLogging(
		/**
		 * Used to define the table name in the database.
		 */
		val tableName: String = JdbcConstants.DEFAULT_TABLE_NAME,
		/**
		 * Used to denote whether or not the logging should run in a separate thread.
		 */
		val runInSeparateThread: Boolean = true,
		/**
		 * By default, logging of failed attempts is enabled. To disable, set this value to false on the annotation.
		 */
		val logFailedAttempts: Boolean = true,
		/**
		 * By default, logging of successful attempts is enabled. To disable, set this value to false on the annotation.
		 */
		val logSuccessfulAttempts: Boolean = true,

		/**
		 * By default, denotes that the database type is a MySQL database (and that is currently the only option), but other options may be available in the future.
		 */
		val databaseType: JdbcDatabaseType = JdbcDatabaseType.MYSQL)
