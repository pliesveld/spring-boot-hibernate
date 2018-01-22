package hello.associations.book.v1;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackageClasses = BookConfig.class)
@EnableTransactionManagement
@EntityScan(basePackageClasses = BookConfig.class)
class BookConfig {
}
