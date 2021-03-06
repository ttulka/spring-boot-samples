package cz.net21.ttulka.test;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.MethodMetadata;

public class OnMissingBeanCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return !conditionContext.getRegistry().containsBeanDefinition(((MethodMetadata) annotatedTypeMetadata).getMethodName());
    }
}
