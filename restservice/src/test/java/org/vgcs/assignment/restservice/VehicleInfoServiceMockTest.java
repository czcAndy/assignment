package org.vgcs.assignment.restservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.vgcs.assignment.restservice.configuration.RestServiceConfig;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseWithIdDTO;
import org.vgcs.assignment.restservice.exception.ExceptionMessages;
import org.vgcs.assignment.restservice.exception.RestCallException;
import org.vgcs.assignment.restservice.impl.VehicleInfoServiceImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {VehicleInfoServiceImpl.class, RestServiceConfig.class})
@TestPropertySource(locations="classpath:application.properties")
@RunWith(SpringRunner.class)
class VehicleInfoServiceMockTest extends GenericServiceTest<VehicleInfoResponseWithIdDTO, VehicleInfoService>{

    @Autowired
    private VehicleInfoService vehicleInfoService;

    @Override
    @Test
    public void test_getResource_200() throws Exception {
        VehicleInfoResponseDTO body = new VehicleInfoResponseDTO("+4678625847", "OK", "Thor's fleet", "Volvo Construction Equipment", "Japan", "000543", "VCE");

        super.enqueueMockResponse(body, 200);
        var response = vehicleInfoService.get("1");

        assert (response.vehicleInfoResponseDTO().msidn().equals("+4678625847"));
        assert (response.vehicleInfoResponseDTO().engineStatus().equals("OK"));
        assert (response.vehicleInfoResponseDTO().fleet().equals("Thor's fleet"));
        assert (response.vehicleInfoResponseDTO().brand().equals("Volvo Construction Equipment"));
        assert (response.vehicleInfoResponseDTO().countryOfOperation().equals("Japan"));
        assert (response.vehicleInfoResponseDTO().chassisNumber().equals("000543"));
        assert (response.vehicleInfoResponseDTO().chassisSeries().equals("VCE"));

    }

    @Override
    @Test
    public void test_getResource_400() throws Exception {
        String body = "{reason: Query param id missing from request.}";
        super.enqueueMockResponse(body, 400);
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get(""));

        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 400);
        assert (exception.getResourceId().equals(""));
    }

    @Override
    @Test
    public void test_getResource_401() throws Exception {
        String body = "{Unauthorized}";
        super.enqueueMockResponse(body, 401);
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 401);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_404() throws Exception {
        String body = "{}";
        super.enqueueMockResponse(body, 404);
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 404);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_500() throws Exception {
        super.enqueueMockResponse("Internal Server Error", 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

        assert (exception.getMessage().equals("Internal Server Error"));
        assert (exception.getErrorCode() == 500);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_nullBody() throws Exception {
        super.enqueueMockResponse(null, 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

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
