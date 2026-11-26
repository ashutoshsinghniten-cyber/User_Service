package com.demo.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Checks how /call/... reacts when order-service (mocked) sends good, changed or broken responses.
@SpringBootTest
@AutoConfigureMockMvc
class RemoteCallControllerTest {

    private static final String DOWNSTREAM = "http://localhost:8081/api/orders/100";
    private static final String VALID = "{\"id\":100,\"userId\":1,\"product_Id\":500,\"quantity\":2}";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RestTemplate rest;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.bindTo(rest).build();
    }

    private void downstreamReturns(String json) {
        server.expect(requestTo(DOWNSTREAM)).andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
    }

    @Test
    void validResponseIsPassedThrough() throws Exception {
        downstreamReturns(VALID);
        mvc.perform(get("/call/orders/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"id\":100,\"userId\":1,\"product_Id\":500,\"quantity\":2,\"extra\":1}",   // extra field
            "{\"id\":100,\"userId\":1,\"product_Id\":500}",                              // missing field
            "{\"id\":100,\"userId\":1,\"productId\":500,\"quantity\":2}",                // renamed field
            "{\"id\":100,\"userId\":1,\"product_Id\":500,\"quantity\":2.7}",             // decimal for int
            "{\"id\":100,\"userId\":1,\"product_Id\":500,\"quantity\":\"2\"}",           // string for int
            "{\"id\":100,\"userId\":1,\"product_Id\":500,\"quantity\":null}",            // null for int
            "{\"id\":null,\"userId\":1,\"product_Id\":500,\"quantity\":2}",              // null field
            "{\"id\":\"100\",\"userId\":1,\"product_Id\":500,\"quantity\":2}",           // string for Long
            "[]",                                                                        // array instead of object
            "null",                                                                      // null body
            ""                                                                           // empty body
    })
    void contractChangeGives502(String json) throws Exception {
        downstreamReturns(json);
        mvc.perform(get("/call/orders/100"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.reason").value(containsString("Contract mismatch")));
    }

    @Test
    void downstream404Gives404() throws Exception {
        server.expect(requestTo(DOWNSTREAM)).andRespond(withStatus(HttpStatus.NOT_FOUND));
        mvc.perform(get("/call/orders/100")).andExpect(status().isNotFound());
    }

    @Test
    void downstream500Gives502WithoutLeakingBody() throws Exception {
        server.expect(requestTo(DOWNSTREAM)).andRespond(withServerError().body("secret-db-host"));
        mvc.perform(get("/call/orders/100"))
                .andExpect(status().isBadGateway())
                .andExpect(content().string(not(containsString("secret-db-host"))));
    }

    @Test
    void downstreamDownGives503WithoutUrl() throws Exception {
        server.expect(requestTo(DOWNSTREAM)).andRespond(withException(new ConnectException("Connection refused")));
        mvc.perform(get("/call/orders/100"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string(not(containsString("localhost"))));
    }

    @Test
    void downstreamTimeoutGives504() throws Exception {
        server.expect(requestTo(DOWNSTREAM)).andRespond(withException(new SocketTimeoutException("Read timed out")));
        mvc.perform(get("/call/orders/100")).andExpect(status().isGatewayTimeout());
    }

    @Test
    void healthEndpointIsUp() throws Exception {
        mvc.perform(get("/actuator/health")).andExpect(status().isOk());
    }
}
