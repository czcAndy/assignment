package org.vgcs.assignment.restservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vgcs.assignment.restservice.configuration.RestServiceConfig;
import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseWithIdDTO;
import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;
import org.vgcs.assignment.restservice.exception.ExceptionMessages;
import org.vgcs.assignment.restservice.exception.RestCallException;
import org.vgcs.assignment.restservice.impl.VehicleServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {VehicleServiceImpl.class, RestServiceConfig.class})
class VehicleServiceMockTest extends GenericServiceTest<VehicleInfoResponseWithIdDTO, VehicleInfoService>{
    @Autowired
    private VehicleService vehicleService;

    @Override
    @Test
    public void test_getResource_200() throws Exception {
        VehicleResponseDTO body = new VehicleResponseDTO(
                List.of(
                        new VehicleDTO("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new VehicleDTO("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));
        super.enqueueMockResponse(body, 200);
        List<VehicleDTO> response = vehicleService.getAll();

        assert (response.get(0).id().equals("bd45a676-0d0e-48b4-9693-e8196eb7fcbf"));
        assert (response.get(0).name().equals("big truck"));

        assert (response.get(1).id().equals("2337d25f-8917-4e26-920f-ddbe9ba063d6"));
        assert (response.get(1).name().equals("small truck"));

        super.enqueueMockResponse(body, 200);
        VehicleDTO response2 = vehicleService.get("bd45a676-0d0e-48b4-9693-e8196eb7fcbf");
        assert (response2.id().equals("bd45a676-0d0e-48b4-9693-e8196eb7fcbf"));
        assert (response2.name().equals("big truck"));
    }

    @Override
    @Test
    public void test_getResource_400() throws Exception {
        String body = "{Bad Request}";
        super.enqueueMockResponse(body, 400);
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        super.enqueueMockResponse(body, 400);
        var exception2 = assertThrows(RestCallException.class, () -> vehicleService.get("1"));

        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 400);
        assert (exception2.getMessage().equals(body));
        assert (exception2.getErrorCode() == 400);
    }

    @Override
    @Test
    public void test_getResource_401() throws Exception {
        String body = "{Unauthorized}";

        super.enqueueMockResponse(body, 401);
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        super.enqueueMockResponse(body, 401);
        var exception2 = assertThrows(RestCallException.class, () -> vehicleService.get("1"));

        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 401);
        assert (exception2.getMessage().equals(body));
        assert (exception2.getErrorCode() == 401);
    }

    @Override
    @Test
    public void test_getResource_404() throws Exception {
        String body = "{Not found}";

        super.enqueueMockResponse(body, 404);
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        super.enqueueMockResponse(body, 404);
        var exception2 = assertThrows(RestCallException.class, () -> vehicleService.get("1"));

        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 404);
        assert (exception2.getMessage().equals(body));
        assert (exception2.getErrorCode() == 404);
    }

    @Override
    @Test
    public void test_getResource_500() throws Exception {
        String body = "{Internal Server Error}";

        super.enqueueMockResponse(body, 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        super.enqueueMockResponse(body, 500);
        var exception2 = assertThrows(RestCallException.class, () -> vehicleService.get("1"));

        assert (exception.getMessage().equals(body));
        assert (exception.getErrorCode() == 500);
        assert (exception2.getMessage().equals(body));
        assert (exception2.getErrorCode() == 500);
    }

    @Override
    @Test
    public void test_getResource_nullBody() throws Exception {

        super.enqueueMockResponse(null, 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        super.enqueueMockResponse(null, 500);
        var exception2 = assertThrows(RestCallException.class, () -> vehicleService.get("1"));

        assert (exception.getMessage().equals(ExceptionMessages.NO_BODY_MESSAGE));
        assert (exception.getErrorCode() == 500);
        assert (exception2.getMessage().equals(ExceptionMessages.NO_BODY_MESSAGE));
        assert (exception2.getErrorCode() == 500);
    }

    @Override
    public void test_getResourceAsync_200() throws Exception {
        VehicleResponseDTO body = new VehicleResponseDTO(
                List.of(
                        new VehicleDTO("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new VehicleDTO("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));

        VehicleResponseDTO body2 = new VehicleResponseDTO(
                List.of(
                        new VehicleDTO("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new VehicleDTO("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));
        super.enqueueMockResponse(body, 200);
        super.enqueueMockResponse(body, 200);

        var response = vehicleService.getAsync(List.of("1","2"));

        assert(response.size() == 2);
        assert (response.stream().anyMatch(r -> r.equals(body)));
        assert (response.stream().anyMatch(r -> r.equals(body2)));
    }

    @Override
    public void test_getResourceAsync_when_at_least_one_200() throws Exception {
        VehicleResponseDTO body = new VehicleResponseDTO(
                List.of(
                        new VehicleDTO("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new VehicleDTO("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));

        VehicleResponseDTO body2 = new VehicleResponseDTO(
                List.of(
                        new VehicleDTO("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new VehicleDTO("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));
        super.enqueueMockResponse(body, 200);
        super.enqueueMockResponse(body, 400);

        var response = vehicleService.getAsync(List.of("1","2"));

        assert(response.size() == 1);
        assert (response.stream().anyMatch(r -> r.equals(body)));
        assert (response.stream().noneMatch(r -> r.equals(body2)));
    }

    @Override
    public void test_getResourceAsync_when_none_200() throws Exception {
        VehicleResponseDTO body = new VehicleResponseDTO(
                List.of(
                        new VehicleDTO("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new VehicleDTO("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));

        VehicleResponseDTO body2 = new VehicleResponseDTO(
                List.of(
                        new VehicleDTO("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new VehicleDTO("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));
        super.enqueueMockResponse(body, 400);
        super.enqueueMockResponse(body, 400);

        var response = vehicleService.getAsync(List.of("1","2"));

        assert(response.isEmpty());
    }

}
