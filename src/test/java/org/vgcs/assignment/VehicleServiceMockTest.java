package org.vgcs.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Value;
import org.vgcs.assignment.dto.VehicleInfoResponseDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vgcs.assignment.dto.VehicleResponseDTO;
import org.vgcs.assignment.model.Vehicle;
import org.vgcs.assignment.services.VehicleService;

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
    void getVehicleInfoResponseDTO_200() throws Exception {
        VehicleInfoResponseDTO vehicleResponseMock = new VehicleInfoResponseDTO("+4678625847", "OK", "Thor's fleet", "Volvo Construction Equipment", "Japan", "000543", "VCE");
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(vehicleResponseMock))
                .addHeader("Content-Type", "application/json"));

        VehicleInfoResponseDTO vehicleResponseMono = vehicleService
                .getVehiclesById("id");

        assert (vehicleResponseMono.msisdn().equals("+4678625847"));
        assert (vehicleResponseMono.engineStatus().equals("OK"));
        assert (vehicleResponseMono.fleet().equals("Thor's fleet"));
        assert (vehicleResponseMono.brand().equals("Volvo Construction Equipment"));
        assert (vehicleResponseMono.countryOfOperation().equals("Japan"));
        assert (vehicleResponseMono.chassisNumber().equals("000543"));
        assert (vehicleResponseMono.chassisSeries().equals("VCE"));
    }

    @Test
    void getVehicleInfoResponseDTO_400() {
        String message = "reason: Query param id missing from request.";
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(400));

        Exception exception = assertThrows(Exception.class, () -> vehicleService.getVehiclesById(""));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicleInfoResponseDTO_404() {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(404));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getVehiclesById("id"));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicleInfoResponseDTO_401() {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(401));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getVehiclesById(""));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicleInfoResponseDTO_500() {
        String message = "Internal Server Error";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(500));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getVehiclesById(""));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicles_200() throws Exception {
        VehicleResponseDTO vehicleResponseMock = new VehicleResponseDTO(
                List.of(
                        new Vehicle("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new Vehicle("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(vehicleResponseMock))
                .addHeader("Content-Type", "application/json"));

        VehicleResponseDTO vehicleResponseMono = vehicleService
                .getVehicleList();

        assert (vehicleResponseMono.vehicles().get(0).id().equals("bd45a676-0d0e-48b4-9693-e8196eb7fcbf"));
        assert (vehicleResponseMono.vehicles().get(0).name().equals("big truck"));

        assert (vehicleResponseMono.vehicles().get(1).id().equals("2337d25f-8917-4e26-920f-ddbe9ba063d6"));
        assert (vehicleResponseMono.vehicles().get(1).name().equals("small truck"));
    }

    @Test
    void getVehicles_404() throws Exception {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(404));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getVehicleList());

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicles_401() throws Exception {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(401));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getVehicleList());

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicles_500() throws Exception {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(500));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getVehicleList());

        assert (exception.getMessage().equals(message));
    }

    @Test
    @Disabled("Until the VehicleService can handle empty String responses")
    void getVehicleInfoResponseDTO_emptyResponseBody_500() {
        String message = "Internal Server Error";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(500));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getVehiclesById(""));
        //TODO: The functionality for the WebClient needs to be revised.
        // Expected to throw an error, instead got a null value for the VehicleInfoResponseDTO
        assert (exception.getMessage().equals(message));
    }

    @Test
    @Disabled("Until the VehicleService can handle empty String responses")
    void getVehicles_emptyResponseBody_500() {
        String message = "Internal Server Error";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(500));
        Exception exception = assertThrows(Exception.class, () -> vehicleService.getVehicleList());
        //TODO: The functionality for the WebClient needs to be revised.
        // Expected to throw an error, instead got a null value for the VehicleInfoResponseDTO
        assert (exception.getMessage().equals(message));
    }
}
