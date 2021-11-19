package org.vgcs.assignment.graphql.configuration;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.vgcs.assignment.restservice.configuration.RestServiceConfig;


@Configuration
@Import(RestServiceConfig.class)
@ComponentScan(basePackages = {"org.vgcs.assignment.persistance","org.vgcs.assignment.restservice"})
@EnableMongoRepositories(basePackages = {"org.vgcs.assignment.persistance"})
public class GraphqlConfig {
    @Bean
    public GraphQLScalarType dateTime() {
        return ExtendedScalars.DateTime;
    }
}
