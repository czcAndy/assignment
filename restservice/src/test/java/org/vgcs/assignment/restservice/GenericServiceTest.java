package org.vgcs.assignment.restservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;


abstract class GenericServiceTest implements ServiceTestSpecifications {
    private static MockWebServer mockWebServer;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(@Value("${service.port}") final int port) throws IOException {
        objectMapper = new ObjectMapper();
        mockWebServer = new MockWebServer();
        mockWebServer.start(port);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    void enqueueMockResponse(Object body, int responseCode)  {
        var response = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(responseCode);

        if (body != null) {
            if (body instanceof String)
                response.setBody((String)body);
            else {
                try {
                    response.setBody(objectMapper.writeValueAsString(body));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        mockWebServer.enqueue(response);
    }


}
