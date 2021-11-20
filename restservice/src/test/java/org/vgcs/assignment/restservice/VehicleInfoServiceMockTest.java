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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {VehicleInfoServiceImpl.class, RestServiceConfig.class})
@TestPropertySource(locations="classpath:application.properties")
@RunWith(SpringRunner.class)
class VehicleInfoServiceMockTest extends GenericServiceTest<VehicleInfoResponseWithIdDTO, VehicleInfoService>{

    @Autowired
    private VehicleInfoService vehicleInfoService;

    @Override
    @Test
    public void test_getResource_200()  {
        VehicleInfoResponseDTO body = new VehicleInfoResponseDTO("+4678625847", "OK", "Thor's fleet", "Volvo Construction Equipment", "Japan", "000543", "VCE");

        super.enqueueMockResponse(body, 200);
        var response = vehicleInfoService.get("1");

        assert (response.vehicleInfoResponseDTO().equals(body));
    }

    @Override
    @Test
    public void test_getResource_400() throws RestCallException {
        String body = "{reason: Query param id missing from request.}";
        super.enqueueMockResponse(body, 400);
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get(""));

        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 400);
        assert (exception.getResourceId().equals(""));
    }

    @Override
    @Test
    public void test_getResource_401() throws RestCallException {
        String body = "{Unauthorized}";
        super.enqueueMockResponse(body, 401);
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 401);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_404() throws RestCallException {
        String body = "{}";
        super.enqueueMockResponse(body, 404);
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 404);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_500() throws RestCallException {
        super.enqueueMockResponse("Internal Server Error", 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

        assert (exception.getMessage().equals("Internal Server Error"));
        assert (exception.getErrorCode() == 500);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResource_nullBody() throws RestCallException {
        super.enqueueMockResponse(null, 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleInfoService.get("1"));

        assert (exception.getMessage().equals(ExceptionMessages.NO_BODY_MESSAGE));
        assert (exception.getErrorCode() == 500);
        assert (exception.getResourceId().equals("1"));
    }

    @Override
    @Test
    public void test_getResourceAsync_200() {
        VehicleInfoResponseDTO body = new VehicleInfoResponseDTO("+4678625847", "OK", "Thor's fleet", "Volvo Construction Equipment", "Japan", "000543", "VCE");
        VehicleInfoResponseDTO body2 = new VehicleInfoResponseDTO("+4635489215", "DANGER", "Thor's fleet", "Volvo Trucks", "Mongolia", "954634", "VT");

        super.enqueueMockResponse(body, 200);
        super.enqueueMockResponse(body2, 200);

        var response = vehicleInfoService.getAsync(List.of("1","2"));

        assert(response.size() == 2);
        assert (response.stream().anyMatch(r -> r.vehicleInfoResponseDTO().equals(body)));
        assert (response.stream().anyMatch(r -> r.vehicleInfoResponseDTO().equals(body2)));
    }

    @Override
    @Test
    public void test_getResourceAsync_when_at_least_one_200()  {
        VehicleInfoResponseDTO body = new VehicleInfoResponseDTO("+4678625847", "OK", "Thor's fleet", "Volvo Construction Equipment", "Japan", "000543", "VCE");
        VehicleInfoResponseDTO body2 = new VehicleInfoResponseDTO("+4635489215", "DANGER", "Thor's fleet", "Volvo Trucks", "Mongolia", "954634", "VT");

        super.enqueueMockResponse(body, 200);
        super.enqueueMockResponse(body2, 400);

        var response = vehicleInfoService.getAsync(List.of("1","2"));

        assert(response.size() == 1);
        assert (response.stream().anyMatch(r -> r.vehicleInfoResponseDTO().equals(body)));
        assert (response.stream().noneMatch(r -> r.vehicleInfoResponseDTO().equals(body2)));
    }

    @Override
    @Test
    public void test_getResourceAsync_when_none_200()  {
        VehicleInfoResponseDTO body = new VehicleInfoResponseDTO("+4678625847", "OK", "Thor's fleet", "Volvo Construction Equipment", "Japan", "000543", "VCE");
        VehicleInfoResponseDTO body2 = new VehicleInfoResponseDTO("+4635489215", "DANGER", "Thor's fleet", "Volvo Trucks", "Mongolia", "954634", "VT");

        super.enqueueMockResponse(body, 400);
        super.enqueueMockResponse(body2, 400);

        var response = vehicleInfoService.getAsync(List.of("1","2"));

        assert(response.isEmpty());
    }

}
