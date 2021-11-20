package org.vgcs.assignment.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vgcs.assignment.restservice.configuration.RestServiceConfig;
import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;
import org.vgcs.assignment.restservice.exception.ExceptionMessages;
import org.vgcs.assignment.restservice.exception.RestCallException;
import org.vgcs.assignment.restservice.impl.VehicleServiceImpl;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {VehicleServiceImpl.class, RestServiceConfig.class})
class VehicleServiceMockTest {
    private static MockWebServer mockWebServer;
    private static ObjectMapper objectMapper;


    @Autowired
    private VehicleService vehicleService;

    @BeforeAll
    static void setup(@Value("${service.port}") final int port) throws IOException {
        objectMapper = new ObjectMapper();
        mockWebServer = new MockWebServer();
        mockWebServer.start(port);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getVehicles_200() throws Exception {
        VehicleResponseDTO vehicleResponseMock = new VehicleResponseDTO(
                List.of(
                        new VehicleDTO("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new VehicleDTO("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(vehicleResponseMock))
                .addHeader("Content-Type", "application/json"));

        List<VehicleDTO> vehicleResponseMono = vehicleService
                .getAll();

        assert (vehicleResponseMono.get(0).id().equals("bd45a676-0d0e-48b4-9693-e8196eb7fcbf"));
        assert (vehicleResponseMono.get(0).name().equals("big truck"));

        assert (vehicleResponseMono.get(1).id().equals("2337d25f-8917-4e26-920f-ddbe9ba063d6"));
        assert (vehicleResponseMono.get(1).name().equals("small truck"));
    }

    @Test
    void getVehicles_404() throws RestCallException {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(404));

        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());

        assert (exception.getMessage().equals(message));
        assert (exception.getErrorCode() == 404);
    }

    @Test
    void getVehicles_401() throws RestCallException {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(401));
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());

        assert (exception.getMessage().equals(message));
        assert (exception.getErrorCode() == 401);
    }

    @Test
    void getVehicles_500() throws RestCallException {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(500));
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());

        assert (exception.getMessage().equals(message));
        assert (exception.getErrorCode() == 500);
    }

    @Test
    void getVehicles_emptyResponseBody_500() {
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(500));
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        assert (exception.getMessage().equals(ExceptionMessages.NO_BODY_MESSAGE));
        assert (exception.getErrorCode() == 500);
    }
}
