package org.vgcs.assignment.restservice;

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
    public void test_getResource_200() {
        VehicleServicesResponseDTO body = new VehicleServicesResponseDTO("ACTIVE",
                List.of(
                        new ServiceDTO("GPS", "ACTIVE", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("FuelMeasurement", "DEACTIVATED", "2019-01-01T09:23:05+01:00")));

        super.enqueueMockResponse(body, 200);

        VehicleServicesResponseWithIdDTO response = vehicleServicesService.get("1");

        assert (response.vehicleServicesResponseDTO().equals(body));
    }

    @Override
    @Test
    public void test_getResource_400() throws RestCallException {
        String body = "{Bad Request}";
        super.enqueueMockResponse(body, 400);
        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get(""));
        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 400);
        assert (exception.getResourceId().equals(""));
    }

    @Override
    @Test
    public void test_getResource_401() throws RestCallException {
        String body = "{Unauthorized}";
        super.enqueueMockResponse(body, 401);
        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get("1"));
        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 401);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_404() throws RestCallException {
        String body = "{Not Found}";
        super.enqueueMockResponse(body, 404);
        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get("1"));
        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 404);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_500() throws RestCallException {
        String body = "{Internal Server Error}";
        super.enqueueMockResponse(body, 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get("1"));
        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 500);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_nullBody() throws RestCallException {
        super.enqueueMockResponse(null, 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleServicesService.get("1"));
        assert (exception.getMessage().equals(ExceptionMessages.NO_BODY_MESSAGE));
        assert (exception.getErrorCode() == 500);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResourceAsync_200() {
        VehicleServicesResponseDTO body = new VehicleServicesResponseDTO("ACTIVE",
                List.of(
                        new ServiceDTO("GPS", "ACTIVE", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("FuelMeasurement", "DEACTIVATED", "2019-01-01T09:23:05+01:00")));

        VehicleServicesResponseDTO body2 = new VehicleServicesResponseDTO("ACTIVE",
                List.of(
                        new ServiceDTO("GPS", "ERROR", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("rswdl", "DEACTIVATED", "2019-01-01T09:23:05+01:00")));

        super.enqueueMockResponse(body, 200);
        super.enqueueMockResponse(body2, 200);

        var response = vehicleServicesService.getAsync(List.of("1","2"));

        assert(response.size() == 2);
        assert (response.stream().anyMatch(r -> r.vehicleServicesResponseDTO().equals(body)));
        assert (response.stream().anyMatch(r -> r.vehicleServicesResponseDTO().equals(body2)));
    }

    @Override
    @Test
    public void test_getResourceAsync_when_at_least_one_200() {
        VehicleServicesResponseDTO body = new VehicleServicesResponseDTO("ACTIVE",
                List.of(
                        new ServiceDTO("GPS", "ACTIVE", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("FuelMeasurement", "DEACTIVATED", "2019-01-01T09:23:05+01:00")));

        VehicleServicesResponseDTO body2 = new VehicleServicesResponseDTO("ACTIVE",
                List.of(
                        new ServiceDTO("GPS", "ERROR", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("rswdl", "DEACTIVATED", "2019-01-01T09:23:05+01:00")));

        super.enqueueMockResponse(body, 200);
        super.enqueueMockResponse(body2, 400);

        var response = vehicleServicesService.getAsync(List.of("1","2"));

        assert(response.size() == 1);
        assert (response.stream().anyMatch(r -> r.vehicleServicesResponseDTO().equals(body)));
    }

    @Override
    public void test_getResourceAsync_when_none_200() {
        VehicleServicesResponseDTO body = new VehicleServicesResponseDTO("ACTIVE",
                List.of(
                        new ServiceDTO("GPS", "ACTIVE", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("FuelMeasurement", "DEACTIVATED", "2019-01-01T09:23:05+01:00")));

        VehicleServicesResponseDTO body2 = new VehicleServicesResponseDTO("ACTIVE",
                List.of(
                        new ServiceDTO("GPS", "ERROR", "2019-01-01T09:23:05+01:00"),
                        new ServiceDTO("rswdl", "DEACTIVATED", "2019-01-01T09:23:05+01:00")));

        super.enqueueMockResponse(body, 400);
        super.enqueueMockResponse(body2, 400);

        var response = vehicleServicesService.getAsync(List.of("1","2"));

        assert(response.isEmpty());
    }
}
