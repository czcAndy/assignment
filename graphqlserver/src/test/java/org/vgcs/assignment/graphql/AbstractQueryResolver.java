package org.vgcs.assignment.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.vgcs.assignment.persistance.VehicleInfoRepo;
import org.vgcs.assignment.persistance.VehicleRepo;
import org.vgcs.assignment.persistance.VehicleServicesRepo;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AssignmentApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@WireMockTest
public class AbstractQueryResolver {
    protected static final String GRAPHQL_QUERY_REQUEST_PATH = "graphql/request/%s.graphql";
    protected static final String GRAPHQL_QUERY_RESPONSE_PATH = "graphql/response/%s.json";
    private static ObjectMapper objectMapper;
    private static WireMockServer wireMockServer;

    @Autowired
    protected GraphQLTestTemplate graphQLTestTemplate;

    @Autowired
    protected VehicleRepo vehicleRepo;

    @Autowired
    protected VehicleInfoRepo vehicleInfoRepo;

    @Autowired
    protected VehicleServicesRepo vehicleServicesRepo;

    @BeforeAll
    static void beforeAll(@Value("${service.port}") final int port) {
        wireMockServer= new WireMockServer(port);
        wireMockServer.start();
        objectMapper = new ObjectMapper();
    }

    @AfterAll
    static void afterAll(){
        wireMockServer.stop();
    }

    @AfterEach
    void afterEach(){
        vehicleInfoRepo.deleteAll();
        vehicleRepo.deleteAll();
        vehicleServicesRepo.deleteAll();
    }

    String read(String location) throws IOException {
        return IOUtils.toString(new ClassPathResource(location).getInputStream());
    }
}
