package com.github.ajthom90.springsecurityloginlogger.configuration.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class FailedLoggerCondition implements Condition {
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		System.out.println("Failed conditional");
		return true;
	}
}
