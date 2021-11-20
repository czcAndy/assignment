package org.vgcs.assignment.restservice;

import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vgcs.assignment.restservice.configuration.RestServiceConfig;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;
import org.vgcs.assignment.restservice.dto.ServiceDTO;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseWithIdDTO;
import org.vgcs.assignment.restservice.exception.ExceptionMessages;
import org.vgcs.assignment.restservice.exception.RestCallException;
import org.vgcs.assignment.restservice.impl.VehicleServicesServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {VehicleServicesServiceImpl.class, RestServiceConfig.class})
class VehicleServicesServiceMockTest extends GenericServiceTest<VehicleServicesResponseWithIdDTO, VehicleServicesService> {

    @Autowired
    private VehicleServicesService vehicleServicesService;

    @Override
    @Test
    public void test_getResource_200() throws Exception {
        VehicleServicesResponseDTO body = new VehicleServicesResponseDTO("ACTIVE",
                List.of(
                        new ServiceDTO("GPS", "ACTIVE", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("FuelMeasurement", "DEACTIVATED", "2019-01-01T09:23:05+01:00")));

        super.enqueueMockResponse(body, 200);

        VehicleServicesResponseWithIdDTO vehicleResponseMono = vehicleServicesService.get("1");

        assert (vehicleResponseMono.vehicleServicesResponseDTO().communicationStatus().equals("ACTIVE"));
        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(0).serviceName().equals("GPS"));
        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(0).status().equals("ACTIVE"));
        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(0).lastUpdate().equals("2019-01-01T09:23:05+01:00"));

        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(1).serviceName().equals("FuelMeasurement"));
        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(1).status().equals("DEACTIVATED"));
        assert (vehicleResponseMono.vehicleServicesResponseDTO().services().get(1).lastUpdate().equals("2019-01-01T09:23:05+01:00"));
    }

    @Override
    @Test
    public void test_getResource_400() throws Exception {
        String body = "{Bad Request}";
        super.enqueueMockResponse(body, 400);
        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get(""));
        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 400);
        assert (exception.getResourceId().equals(""));
    }

    @Override
    @Test
    public void test_getResource_401() throws Exception {
        String body = "{Unauthorized}";
        super.enqueueMockResponse(body, 401);
        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get("1"));
        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 401);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_404() throws Exception {
        String body = "{Not Found}";
        super.enqueueMockResponse(body, 404);
        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get("1"));
        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 404);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_500() throws Exception {
        String body = "{Internal Server Error}";
        super.enqueueMockResponse(body, 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get("1"));
        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 500);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_nullBody() throws Exception {
        super.enqueueMockResponse(null, 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get("1"));
        assert (exception.getMessage().equals(ExceptionMessages.NO_BODY_MESSAGE));
        assert (exception.getErrorCode() == 500);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    public void test_getResourceAsync_200() {

    }

    @Override
    public void test_getResourceAsync_400() {

    }

    @Override
    public void test_getResourceAsync_401() {

    }

    @Override
    public void test_getResourceAsync_404() {

    }

    @Override
    public void test_getResourceAsync_500() {

    }
}
