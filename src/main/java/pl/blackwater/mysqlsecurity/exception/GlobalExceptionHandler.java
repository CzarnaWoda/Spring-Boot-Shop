package pl.blackwater.mysqlsecurity.exception;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.blackwater.mysqlsecurity.data.HttpResponse;

import java.nio.file.AccessDeniedException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
@ComponentScan
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<HttpResponse> handleAuthException(AuthException e){
        return new ResponseEntity<>(e.getHttpResponse(),e.getHttpResponse().getStatus());
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> handleAccessDeniedException(AccessDeniedException e) {
        HttpResponse response = HttpResponse.builder()
                .status(FORBIDDEN)
                .statusCode(FORBIDDEN.value())
                .message(e.getMessage())
                .developerMessage("ACCESS DENIED EXCEPTION")
                .build();
        return new ResponseEntity<>(response, FORBIDDEN);
    }
}
