package br.com.controleestacionamento.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
public class ParkingSpotExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandartError> notFoundException(IllegalArgumentException e, HttpServletRequest req) {
        StandartError error = new StandartError();
        error.setTimestamp(LocalDateTime.now(ZoneId.of("UTC")));
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError(e.getMessage());
        error.setPath(req.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
