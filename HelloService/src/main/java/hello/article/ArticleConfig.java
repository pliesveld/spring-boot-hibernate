package hello.article;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "hello.article")
@EnableTransactionManagement
@EntityScan(basePackages = "hello.article")
public class ArticleConfig {
}
