package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableConfigurationProperties(EclipselinkProperties.class)
@ConditionalOnSingleCandidate(DataSource.class)
public class EclipselinkJpaConfiguration extends JpaBaseConfiguration {

    private final EclipselinkProperties eclipselinkProperties;

    EclipselinkJpaConfiguration(DataSource dataSource,
                                JpaProperties jpaProperties,
                                ObjectProvider<JtaTransactionManager> jtaTransactionManager,
                                ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers,
                                EclipselinkProperties eclipselinkProperties) {
        super(dataSource, jpaProperties, jtaTransactionManager, transactionManagerCustomizers);
        this.eclipselinkProperties = eclipselinkProperties;
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        return new LinkedHashMap<>(eclipselinkProperties.getProperties());
    }
}
