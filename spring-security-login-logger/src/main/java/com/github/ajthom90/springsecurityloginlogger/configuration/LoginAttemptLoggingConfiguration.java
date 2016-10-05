package com.github.ajthom90.springsecurityloginlogger.configuration;

import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

public class LoginAttemptLoggingConfiguration implements ImportAware {

	private String tableName;
	
	private boolean runInSeparateThread;
	
	private boolean logFailedAttempts;
	
	private boolean logSuccessfulAttempts;
	
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		// TODO Auto-generated method stub
		
	}
}
