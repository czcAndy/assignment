package org.vgcs.assignment.graphql;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.core.io.ClassPathResource;
import org.vgcs.assignment.persistance.VehicleInfoRepo;
import org.vgcs.assignment.persistance.VehicleRepo;
import org.vgcs.assignment.persistance.VehicleServicesRepo;
import org.vgcs.assignment.restservice.VehicleInfoService;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.VehicleServicesService;
import org.vgcs.assignment.restservice.dto.*;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest
public class AbstractQueryResolver {
    protected static final String GRAPHQL_QUERY_REQUEST_PATH = "graphql/request/%s.graphql";
    protected static final String GRAPHQL_QUERY_RESPONSE_PATH = "graphql/response/%s.json";
    private static ObjectMapper objectMapper;
    private static  WireMockServer wireMockServer;

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

    void wireMockResponse(String value, int responseCode, String uri) throws JsonProcessingException {
        wireMockServer.stubFor(get(urlEqualTo(String.format("/vehicle%s", uri)))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(value)
                        .withStatus(responseCode)));

    }

    String read(String location) throws IOException {
        return IOUtils.toString(new ClassPathResource(location).getInputStream());
    }


    void mockVehicleList() throws JsonProcessingException {
        VehicleResponseDTO body = new VehicleResponseDTO(
                List.of(
                        new VehicleDTO("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new VehicleDTO("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));
        wireMockResponse(objectMapper.writeValueAsString(body), 200, "/list/");
    }

    void mockVehicleInfo(String id, int statusCode) throws JsonProcessingException {
        VehicleInfoResponseDTO body = null;
        String errorMessage = "{Cannot fetch resource}";
        switch (id) {
            case ("bd45a676-0d0e-48b4-9693-e8196eb7fcbf"):
                body = new VehicleInfoResponseDTO("+4678625847", "OK", "Thor's fleet", "Volvo Construction Equipment", "Japan", "000543", "VCE");
                break;
            case ("2337d25f-8917-4e26-920f-ddbe9ba063d6"):
                body = new VehicleInfoResponseDTO("+4635489215", "DANGER", "Thor's fleet", "Volvo Trucks", "Mongolia", "954634", "VT");
                break;
            default:
                return;
        }

        if(statusCode == 400 || statusCode == 401 || statusCode == 403 || statusCode == 500 || body == null)
            wireMockResponse(errorMessage, statusCode, "/info?id=" + id);
        else
            wireMockResponse(objectMapper.writeValueAsString(body), statusCode, "/info?id="+id);
    }

    void mockVehicleServices(String id, int statusCode) throws JsonProcessingException {
        VehicleServicesResponseDTO body = null;
        String errorMessage = "{Cannot fetch resource}";

        switch (id) {
            case ("bd45a676-0d0e-48b4-9693-e8196eb7fcbf"):
                body = new VehicleServicesResponseDTO("ACTIVE", List.of(
                        new ServiceDTO("GPS", "ACTIVE", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("FuelMeasurement", "DEACTIVATED", "2019-01-01T09:23:05+01:00")));
                break;
            case ("2337d25f-8917-4e26-920f-ddbe9ba063d6"):
                body = new VehicleServicesResponseDTO("ACTIVE", List.of(
                        new ServiceDTO("GPS", "ACTIVE", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("FuelMeasurement", "ACTIVATED", "2019-01-01T09:23:05+01:00")));
                break;
            default:
                return;
        }

        if(statusCode == 400 || statusCode == 401 || statusCode == 403 || statusCode == 500 || body == null)
            wireMockResponse(errorMessage, statusCode, "/services?id="+id);
        else
            wireMockResponse(objectMapper.writeValueAsString(body), statusCode, "/services?id="+id);
    }

}
