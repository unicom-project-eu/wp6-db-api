package it.datawizard.unicom.unicombackend.elasticsearch;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "it.datawizard.unicom.unicombackend.elasticsearch.repository")
public class ElasticSearchConfiguration {
}
