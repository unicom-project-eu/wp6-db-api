//package it.datawizard.unicom.unicombackend.elasticsearch;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import co.elastic.clients.transport.ElasticsearchTransport;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import org.elasticsearch.client.RestClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "it.datawizard.unicom.unicombackend.elasticsearch.repository")
//public class ElasticSearchConfiguration {
//    @Bean
//    public ElasticsearchClient client() {
//        return null;
//        ClientConfiguration clientConfiguration
//                = ClientConfiguration.builder()
//                .connectedTo("localhost:9200")
//                .build();
//
//        RestClient restClient = RestClients.create(clientConfiguration).lowLevelRest();
//
//        ElasticsearchTransport transport = new RestClientTransport(
//                restClient, new JacksonJsonpMapper());
//
//        ElasticsearchClient client = new ElasticsearchClient(transport);
//        return client;
//    }
//
////    @Bean
////    public ElasticsearchOperations elasticsearchTemplate() {
////        return new ElasticsearchRestTemplate(client());
////    }
//}
