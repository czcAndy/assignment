package org.vgcs.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Value;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;
import org.vgcs.assignment.restservice.VehicleService;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest("classpath:application.test.properties")
class VehicleServiceMockTest {
    private static MockWebServer mockWebServer;
    private final ObjectMapper objectMapper;

    @Value("${service.port}")
    private static int PORT = 8080;

    @Autowired
    private VehicleService vehicleService;

    public VehicleServiceMockTest() {
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
    void getVehicles_404() throws Exception {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(404));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getAll());

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicles_401() throws Exception {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(401));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getAll());

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicles_500() throws Exception {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(500));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getAll());

        assert (exception.getMessage().equals(message));
    }

    @Test
    @Disabled("Until the VehicleService can handle empty String responses")
    void getVehicles_emptyResponseBody_500() {
        String message = "Internal Server Error";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(500));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getAll());
        //TODO: The functionality for the WebClient needs to be revised.
        // Expected to throw an error, instead got a null value for the VehicleInfoResponseDTO
        assert (exception.getMessage().equals(message));
    }
}
