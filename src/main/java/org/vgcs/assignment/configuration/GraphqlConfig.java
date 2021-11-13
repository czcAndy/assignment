package org.vgcs.assignment.configuration;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphqlConfig {
    @Bean
    public GraphQLScalarType dateTime() {
        return ExtendedScalars.DateTime;
    }
}
