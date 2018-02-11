package hello.tracker.v1;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackageClasses = TrackerConfig.class)
@EnableTransactionManagement
@EntityScan(basePackageClasses = TrackerConfig.class)
class TrackerConfig {}
