package org.vgcs.assignment.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.vgcs.assignment.restservice.configuration.RestServiceConfig;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseWithIdDTO;
import org.vgcs.assignment.restservice.exception.ExceptionMessages;
import org.vgcs.assignment.restservice.exception.RestCallException;
import org.vgcs.assignment.restservice.impl.VehicleInfoServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {VehicleInfoServiceImpl.class, RestServiceConfig.class})
@TestPropertySource(locations="classpath:application.properties")
@RunWith(SpringRunner.class)
class VehicleInfoServiceMockTest {

    private static MockWebServer mockWebServer;
    private final ObjectMapper objectMapper;

    @Autowired
    private VehicleInfoService vehicleInfoService;

    public VehicleInfoServiceMockTest() {
        objectMapper = new ObjectMapper();
    }

    @BeforeAll
    static void setUp(@Value("${service.port}") final int port) throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(port);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getVehicleInfo_200() throws Exception {
        VehicleInfoResponseDTO vehicleResponseMock = new VehicleInfoResponseDTO("+4678625847", "OK", "Thor's fleet", "Volvo Construction Equipment", "Japan", "000543", "VCE");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(vehicleResponseMock))
                .addHeader("Content-Type", "application/json"));

        VehicleInfoResponseWithIdDTO vehicleResponseMono = vehicleInfoService
                .get("id");

        assert (vehicleResponseMono.vehicleInfoResponseDTO().msidn().equals("+4678625847"));
        assert (vehicleResponseMono.vehicleInfoResponseDTO().engineStatus().equals("OK"));
        assert (vehicleResponseMono.vehicleInfoResponseDTO().fleet().equals("Thor's fleet"));
        assert (vehicleResponseMono.vehicleInfoResponseDTO().brand().equals("Volvo Construction Equipment"));
        assert (vehicleResponseMono.vehicleInfoResponseDTO().countryOfOperation().equals("Japan"));
        assert (vehicleResponseMono.vehicleInfoResponseDTO().chassisNumber().equals("000543"));
        assert (vehicleResponseMono.vehicleInfoResponseDTO().chassisSeries().equals("VCE"));
    }

    @Test
    void getVehicleInfo_400() throws RestCallException {
        String message = "reason: Query param id missing from request.";
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(400));

        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get(""));

        assert (exception.getMessage().equals(message));
        assert (exception.getErrorCode() == 400);
        assert (exception.getResourceId().equals(""));
    }

    @Test
    void getVehicleInfo_404() throws RestCallException {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(404));
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

        assert (exception.getMessage().equals(message));
        assert (exception.getErrorCode() == 404);
        assert (exception.getResourceId().equals("1"));
    }

    @Test
    void getVehicleInfo_401() throws RestCallException {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(401));
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

        assert (exception.getMessage().equals(message));
        assert (exception.getErrorCode() == 401);
        assert (exception.getResourceId().equals("1"));
    }

    @Test
    void getVehicleInfo_500() throws RestCallException {
        String message = "Internal Server Error";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(500));
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

        assert (exception.getMessage().equals(message));
        assert (exception.getErrorCode() == 500);
        assert (exception.getResourceId().equals("1"));
    }

    @Test
    void getVehicleInfo_emptyResponseBody_500() throws RestCallException{
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(500));
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));
        assert (exception.getMessage().equals(ExceptionMessages.NO_BODY_MESSAGE));
        assert (exception.getErrorCode() == 500);
        assert (exception.getResourceId().equals("1"));
    }

}
