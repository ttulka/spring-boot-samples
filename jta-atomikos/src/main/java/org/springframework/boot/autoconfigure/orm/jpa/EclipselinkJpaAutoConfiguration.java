package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ClassUtils;

@Configuration
@ConditionalOnClass({
        LocalContainerEntityManagerFactoryBean.class,
        EnableTransactionManagement.class,
        EntityManager.class})
@Conditional(EclipselinkJpaAutoConfiguration.EclipseLinkEntityManagerCondition.class)
@EnableConfigurationProperties(JpaProperties.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@Import(EclipselinkJpaConfiguration.class)
public class EclipselinkJpaAutoConfiguration {

    @Order(Ordered.HIGHEST_PRECEDENCE + 20)
    static class EclipseLinkEntityManagerCondition extends SpringBootCondition {

        private static String[] CLASS_NAMES = {
                "org.eclipse.persistence.jpa.JpaEntityManager"
        };

        @Override
        public ConditionOutcome getMatchOutcome(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
            for (String className : CLASS_NAMES) {
                if (ClassUtils.isPresent(className, context.getClassLoader())) {
                    return ConditionOutcome.match("found " + className + " class");
                }
            }
            return ConditionOutcome.noMatch("did not find " + Arrays.toString(CLASS_NAMES) + " class(es)");
        }

    }
}
