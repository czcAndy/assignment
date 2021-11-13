package org.vgcs.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.vgcs.assignment.dto.VehicleServicesResponseDTO;
import org.vgcs.assignment.model.Service;
import org.vgcs.assignment.services.impl.VehicleServicesServiceImpl;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest("classpath:application.test.properties")
class VehicleServicesServiceMockTest {
    private static MockWebServer mockWebServer;
    private final ObjectMapper objectMapper;

    @Value("${service.port}")
    private static int PORT = 8080;

    @Autowired
    private VehicleServicesServiceImpl vehicleServicesService;

    public VehicleServicesServiceMockTest() {
        objectMapper = new ObjectMapper();
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(PORT);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getVehicleServicesResponseDTO_200() throws Exception {
        VehicleServicesResponseDTO vehicleServicesResponseMock = new VehicleServicesResponseDTO("ACTIVE",
                List.of(
                        new Service("GPS", "ACTIVE", Date.from(Instant.parse("2019-01-01T09:23:05+01:00"))),
                        new Service("FuelMeasurement", "DEACTIVATED", Date.from(Instant.parse("2019-01-01T09:23:05+01:00")))));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(vehicleServicesResponseMock))
                .addHeader("Content-Type", "application/json"));

        VehicleServicesResponseDTO vehicleResponseMono = vehicleServicesService
                .getVehicleServices("id");

        assert (vehicleResponseMono.communicationStatus().equals("ACTIVE"));

        assert (vehicleResponseMono.services().get(0).serviceName().equals("GPS"));
        assert (vehicleResponseMono.services().get(0).status().equals("ACTIVE"));
        assert (vehicleResponseMono.services().get(0).lastUpdated().equals(Date.from(Instant.parse("2019-01-01T09:23:05+01:00"))));

        assert (vehicleResponseMono.services().get(1).serviceName().equals("FuelMeasurement"));
        assert (vehicleResponseMono.services().get(1).status().equals("DEACTIVATED"));
        assert (vehicleResponseMono.services().get(1).lastUpdated().equals(Date.from(Instant.parse("2019-01-01T09:23:05+01:00"))));
    }

    @Test
    void getVehicleServicesResponseDTO_400()  {
        String message = "reason: Query param id missing from request.";
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(400));

        Exception exception = assertThrows(Exception.class, () -> vehicleServicesService.getVehicleServices(""));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicleServicesResponseDTO_401()  {
        String message = "{}";
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(401));

        Exception exception = assertThrows(Exception.class, () -> vehicleServicesService.getVehicleServices(""));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicleServicesResponseDTO_500()  {
        String message = "Internal Server Error";
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(500));

        Exception exception = assertThrows(Exception.class, () -> vehicleServicesService.getVehicleServices(""));

        assert (exception.getMessage().equals(message));
    }

}
