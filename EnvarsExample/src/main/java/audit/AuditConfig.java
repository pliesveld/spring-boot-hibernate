package audit;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackageClasses = AuditConfig.class)
@EnableJpaAuditing
@EnableTransactionManagement
@EntityScan(basePackageClasses = AuditConfig.class)
class AuditConfig {}
