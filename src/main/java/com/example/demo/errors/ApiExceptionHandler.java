package com.example.demo.errors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiBaseException.class)
    public ResponseEntity<ErrorDetails>  handleApiException (ApiBaseException exception, WebRequest request)
    {
        ErrorDetails details = new ErrorDetails(exception.getMessage(),request.getDescription(true));
        return new ResponseEntity<>(details, exception.getStatusCode());
    }

}
