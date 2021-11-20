package org.vgcs.assignment.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.vgcs.assignment.restservice.configuration.RestServiceConfig;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;
import org.vgcs.assignment.restservice.dto.ServiceDTO;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseWithIdDTO;
import org.vgcs.assignment.restservice.exception.ExceptionMessages;
import org.vgcs.assignment.restservice.exception.RestCallException;
import org.vgcs.assignment.restservice.impl.VehicleServicesServiceImpl;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {VehicleServicesServiceImpl.class, RestServiceConfig.class})
class VehicleServicesServiceMockTest {
    private static MockWebServer mockWebServer;
    private final ObjectMapper objectMapper;

    @Autowired
    private VehicleServicesServiceImpl vehicleServicesService;

    public VehicleServicesServiceMockTest() {
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
    void getVehicleServices_200() throws Exception {
        VehicleServicesResponseDTO vehicleServicesResponseMock = new VehicleServicesResponseDTO("ACTIVE",
                List.of(
                        new ServiceDTO("GPS", "ACTIVE", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("FuelMeasurement", "DEACTIVATED", "2019-01-01T09:23:05+01:00")));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(vehicleServicesResponseMock))
                .addHeader("Content-Type", "application/json"));

        VehicleServicesResponseWithIdDTO vehicleResponseMono = vehicleServicesService
                .get("id");

        assert (vehicleResponseMono.vehicleServicesResponseDTO().communicationStatus().equals("ACTIVE"));

        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(0).serviceName().equals("GPS"));
        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(0).status().equals("ACTIVE"));
        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(0).lastUpdate().equals("2019-01-01T09:23:05+01:00"));

        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(1).serviceName().equals("FuelMeasurement"));
        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(1).status().equals("DEACTIVATED"));
        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(1).lastUpdate().equals("2019-01-01T09:23:05+01:00"));
    }

    @Test
    void getVehicleServices_400() throws RestCallException {
        String message = "reason: Query param id missing from request.";
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(400));

        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get(""));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicleServices_401() throws RestCallException  {
        String message = "{}";
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(401));

        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get(""));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicleServices_500() throws RestCallException {
        String message = "Internal Server Error";
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(500));

        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get(""));

        assert (exception.getMessage().equals("Internal Server Error"));
    }

    @Test
    void getVehicleServices_emptyResponse_500() throws RestCallException {
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(500));

        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get(""));

        assert (exception.getMessage().equals(ExceptionMessages.NO_BODY_MESSAGE));
    }

}
