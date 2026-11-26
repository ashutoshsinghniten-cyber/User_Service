package com.demo.user.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.net.SocketTimeoutException;
import java.util.Map;

<<<<<<< HEAD
// Turns failed calls to the other services into clear responses.
// Full details go to the log; the client gets a short reason without internal URLs or bodies.
@RestControllerAdvice
=======
@RestControllerAdvice
   
>>>>>>> b47b55c7c7f4a49c061c6e911bf5dcb3aa94c98a
public class DownstreamExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(DownstreamExceptionHandler.class);

    // Downstream said 404 -> the resource really does not exist
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<Map<String, String>> notFound(HttpClientErrorException.NotFound e) {
        log.warn("Downstream returned 404: {}", e.getMessage());
        return error(HttpStatus.NOT_FOUND, "Not found in downstream service");
    }

    // Downstream answered with another error status (4xx / 5xx)
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, String>> errorStatus(RestClientResponseException e) {
        log.warn("Downstream returned {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
        return error(HttpStatus.BAD_GATEWAY, "Downstream returned status " + e.getStatusCode().value());
    }

    // Downstream is down or too slow
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, String>> unreachable(ResourceAccessException e) {
        log.warn("Downstream unreachable", e);
        return e.getCause() instanceof SocketTimeoutException
                ? error(HttpStatus.GATEWAY_TIMEOUT, "Downstream service timed out")
                : error(HttpStatus.SERVICE_UNAVAILABLE, "Downstream service unavailable");
    }

    // Anything else, mainly a broken API contract (bad JSON / changed fields / empty body)
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, String>> contract(RestClientException e) {
        log.warn("Downstream call failed", e);
        Throwable cause = NestedExceptionUtils.getMostSpecificCause(e);
        String reason = cause instanceof JsonMappingException jme ? jme.getOriginalMessage() : cause.getMessage();
        return error(HttpStatus.BAD_GATEWAY, "Contract mismatch: " + reason);
    }

    private static ResponseEntity<Map<String, String>> error(HttpStatus status, String reason) {
        return ResponseEntity.status(status).body(Map.of("error", "Downstream call failed", "reason", reason));
    }
}
