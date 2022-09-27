package it.datawizard.unicom.unicombackend.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "it.datawizard.unicom.unicombackend.jpa.repository")
public class JpaConfiguration {

}
