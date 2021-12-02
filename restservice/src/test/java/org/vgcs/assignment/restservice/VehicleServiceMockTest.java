package org.vgcs.assignment.restservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vgcs.assignment.restservice.configuration.RestServiceConfig;
import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;
import org.vgcs.assignment.restservice.exception.ExceptionMessages;
import org.vgcs.assignment.restservice.exception.RestCallException;
import org.vgcs.assignment.restservice.impl.VehicleServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {VehicleServiceImpl.class, RestServiceConfig.class})
class VehicleServiceMockTest extends GenericServiceTest{
    @Autowired
    private VehicleService vehicleService;

    @Override
    @Test
    public void test_getResource_200() throws RestCallException {
        VehicleResponseDTO body = new VehicleResponseDTO(
                List.of(
                        new VehicleDTO("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", "big truck"),
                        new VehicleDTO("2337d25f-8917-4e26-920f-ddbe9ba063d6", "small truck")
                ));
        super.enqueueMockResponse(body, 200);
        List<VehicleDTO> response = vehicleService.getAll();

        assertEquals("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", response.get(0).id());
        assertEquals("big truck", response.get(0).name());

        assertEquals("2337d25f-8917-4e26-920f-ddbe9ba063d6", response.get(1).id());
        assertEquals("small truck", response.get(1).name());

        super.enqueueMockResponse(body, 200);
        VehicleDTO response2 = vehicleService.get("bd45a676-0d0e-48b4-9693-e8196eb7fcbf");
        assertEquals("bd45a676-0d0e-48b4-9693-e8196eb7fcbf", response2.id());
        assertEquals("big truck", response2.name());
    }

    @Override
    @Test
    public void test_getResource_400() throws RestCallException {
        String body = "{Bad Request}";
        super.enqueueMockResponse(body, 400);
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        super.enqueueMockResponse(body, 400);
        var exception2 = assertThrows(RestCallException.class, () -> vehicleService.get("1"));

        assertEquals(exception.getMessage(), body);
        assertEquals(400, exception.getErrorCode());
        assertEquals(exception2.getMessage(), body);
        assertEquals(400, exception2.getErrorCode());
    }

    @Override
    @Test
    public void test_getResource_401() throws RestCallException {
        String body = "{Unauthorized}";

        super.enqueueMockResponse(body, 401);
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        super.enqueueMockResponse(body, 401);
        var exception2 = assertThrows(RestCallException.class, () -> vehicleService.get("1"));

        assertEquals(exception.getMessage(), body);
        assertEquals(401, exception.getErrorCode());
        assertEquals(exception2.getMessage(), body);
        assertEquals(401, exception2.getErrorCode());
    }

    @Override
    @Test
    public void test_getResource_404() throws RestCallException {
        String body = "{Not found}";

        super.enqueueMockResponse(body, 404);
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        super.enqueueMockResponse(body, 404);
        var exception2 = assertThrows(RestCallException.class, () -> vehicleService.get("1"));

        assertEquals(exception.getMessage(), body);
        assertEquals(404, exception.getErrorCode());
        assertEquals(exception2.getMessage(), body);
        assertEquals(404, exception2.getErrorCode());
    }

    @Override
    @Test
    public void test_getResource_500() throws RestCallException {
        String body = "{Internal Server Error}";

        super.enqueueMockResponse(body, 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        super.enqueueMockResponse(body, 500);
        var exception2 = assertThrows(RestCallException.class, () -> vehicleService.get("1"));

        assertEquals(exception.getMessage(), body);
        assertEquals(500, exception.getErrorCode());
        assertEquals(exception2.getMessage(), body);
        assertEquals(500, exception2.getErrorCode());
    }

    @Override
    @Test
    public void test_getResource_nullBody() throws RestCallException {

        super.enqueueMockResponse(null, 500);
        var exception = assertThrows(RestCallException.class, () -> vehicleService.getAll());
        super.enqueueMockResponse(null, 500);
        var exception2 = assertThrows(RestCallException.class, () -> vehicleService.get("1"));

        assertEquals(ExceptionMessages.NO_BODY_MESSAGE, exception.getMessage());
        assertEquals(500, exception.getErrorCode());
        assertEquals(ExceptionMessages.NO_BODY_MESSAGE, exception2.getMessage());
        assertEquals(500, exception2.getErrorCode());
    }

    @Override
    @Disabled("Not applicable")
    public void test_getResourceAsync_200() {

    }

    @Override
    @Disabled("Not applicable")
    public void test_getResourceAsync_when_at_least_one_200() {

    }

    @Override
    @Disabled("Not applicable")
    public void test_getResourceAsync_when_none_200() {

    }

}
