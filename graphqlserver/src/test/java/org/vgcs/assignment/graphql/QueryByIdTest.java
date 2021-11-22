package org.vgcs.assignment.graphql;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.graphql.spring.boot.test.GraphQLResponse;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AssignmentApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class QueryByIdTest extends AbstractQueryResolver {


    @Test
    void resolve_vehicles_OK_test() throws IOException, JSONException {
        var queryPath = "queryById/request";
        var responsePath = "queryById/response_exists";

        mockVehicleList();
        mockVehicleServices("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", 200);
        mockVehicleInfo("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", 200);

        GraphQLResponse response = graphQLTestTemplate.postForResource(String.format(GRAPHQL_QUERY_REQUEST_PATH, queryPath));
        var expectedResponseBody = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, responsePath));

        assert (response.isOk());
        JSONAssert.assertEquals(expectedResponseBody, response.getRawResponse().getBody(), true);
        assert (vehicleRepo.findById("bd45a676-0d0e-48b4-9693-e8196eb7fcbf").isPresent());
        assert (vehicleInfoRepo.findById("bd45a676-0d0e-48b4-9693-e8196eb7fcbf").isPresent());
        assert (vehicleServicesRepo.findById("bd45a676-0d0e-48b4-9693-e8196eb7fcbf").isPresent());
    }

    @Test
    void resolve_vehicles_error_test() throws IOException, JSONException {
        var requestPath = "queryById/request";
        var responsePath = "queryById/response_error";

        mockVehicleList();
        mockVehicleServices("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", 401);
        mockVehicleInfo("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", 401);

        GraphQLResponse response = graphQLTestTemplate.postForResource(String.format(GRAPHQL_QUERY_REQUEST_PATH, requestPath));
        var expectedResponseBody = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, responsePath));

        assert (response.isOk());
        JSONAssert.assertEquals(expectedResponseBody, response.getRawResponse().getBody(), true);
        assert (vehicleRepo.findById("bd45a676-0d0e-48b4-9693-e8196eb7fcbf").isPresent());
        assert (vehicleInfoRepo.findById("bd45a676-0d0e-48b4-9693-e8196eb7fcbf").isEmpty());
        assert (vehicleServicesRepo.findById("bd45a676-0d0e-48b4-9693-e8196eb7fcbf").isEmpty());
    }
}
