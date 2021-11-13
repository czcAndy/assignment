package org.vgcs.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.vgcs.assignment.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.services.VehicleInfoService;
import org.vgcs.assignment.services.impl.VehicleServicesServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest("classpath:application.test.properties")
class VehicleInfoServiceMockTest {

    private static MockWebServer mockWebServer;
    private final ObjectMapper objectMapper;

    @Value("${service.port}")
    private static int PORT = 8080;

    @Autowired
    private VehicleInfoService vehicleInfoService;

    public VehicleInfoServiceMockTest() {
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

        VehicleInfoResponseDTO vehicleResponseMono = vehicleInfoService
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

        Exception exception = assertThrows(Exception.class, () -> vehicleInfoService.getVehiclesById(""));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicleInfoResponseDTO_404() {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(404));
        Exception exception = assertThrows(Exception.class, () -> vehicleInfoService.getVehiclesById("id"));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicleInfoResponseDTO_401() {
        String message = "{}";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(401));
        Exception exception = assertThrows(Exception.class, () -> vehicleInfoService.getVehiclesById("id"));

        assert (exception.getMessage().equals(message));
    }

    @Test
    void getVehicleInfoResponseDTO_500() {
        String message = "Internal Server Error";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(message)
                .setResponseCode(500));
        Exception exception = assertThrows(Exception.class, () -> vehicleInfoService.getVehiclesById("id"));

        assert (exception.getMessage().equals(message));
    }

    @Test
    @Disabled("Until the VehicleService can handle empty String responses")
    void getVehicleInfoResponseDTO_emptyResponseBody_500() {
        String message = "Internal Server Error";

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(500));
        Exception exception = assertThrows(Exception.class, () -> vehicleInfoService.getVehiclesById("id"));
        //TODO: The functionality for the WebClient needs to be revised.
        // Expected to throw an error, instead got a null value for the VehicleInfoResponseDTO
        assert (exception.getMessage().equals(message));
    }

}
