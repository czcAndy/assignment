package org.vgcs.assignment.graphql;

import com.graphql.spring.boot.test.GraphQLResponse;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryByIdTest extends AbstractQueryResolver {

    @Test
    void resolve_vehicles_OK_test() throws IOException, JSONException {
        var queryPath = "queryById/request_exists";
        var responsePath = "queryById/response_exists";

        GraphQLResponse response = graphQLTestTemplate.postForResource(String.format(GRAPHQL_QUERY_REQUEST_PATH, queryPath));
        var expectedResponseBody = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, responsePath));

        assertTrue(response.isOk());
        JSONAssert.assertEquals(expectedResponseBody, response.getRawResponse().getBody(), true);
        assertTrue(vehicleRepo.findById("bd45a676-0d0e-48b4-9693-e8196eb7fcbf").isPresent());
        assertTrue(vehicleInfoRepo.findById("bd45a676-0d0e-48b4-9693-e8196eb7fcbf").isPresent());
        assertTrue(vehicleServicesRepo.findById("bd45a676-0d0e-48b4-9693-e8196eb7fcbf").isPresent());
    }

    @Test
    void resolve_vehicles_error_test() throws IOException, JSONException {
        var requestPath = "queryById/request_error";
        var responsePath = "queryById/response_error";

        GraphQLResponse response = graphQLTestTemplate.postForResource(String.format(GRAPHQL_QUERY_REQUEST_PATH, requestPath));
        var expectedResponseBody = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, responsePath));

        assertTrue(response.isOk());
        JSONAssert.assertEquals(expectedResponseBody, response.getRawResponse().getBody(), true);
        assertTrue(vehicleRepo.findById("f7c8402d-98ca-4728-9c3e-0805c3ecffbb").isPresent());
        assertTrue(vehicleInfoRepo.findById("f7c8402d-98ca-4728-9c3e-0805c3ecffbb").isEmpty());
        assertTrue(vehicleServicesRepo.findById("f7c8402d-98ca-4728-9c3e-0805c3ecffbb").isEmpty());
    }

    @Test
    void resolve_vehicles_notFound_test() throws IOException, JSONException {
        var requestPath = "queryById/request_not_found";
        var responsePath = "queryById/response_not_found";

        GraphQLResponse response = graphQLTestTemplate.postForResource(String.format(GRAPHQL_QUERY_REQUEST_PATH, requestPath));
        var expectedResponseBody = read(String.format(GRAPHQL_QUERY_RESPONSE_PATH, responsePath));

        assertTrue(response.isOk());
        JSONAssert.assertEquals(expectedResponseBody, response.getRawResponse().getBody(), true);
        assertTrue(vehicleRepo.findById("dummy").isEmpty());
        assertTrue(vehicleInfoRepo.findById("dummy").isEmpty());
        assertTrue(vehicleServicesRepo.findById("dummy").isEmpty());
    }
}
