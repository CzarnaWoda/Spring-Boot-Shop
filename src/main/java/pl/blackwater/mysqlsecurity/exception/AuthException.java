package pl.blackwater.mysqlsecurity.exception;

import lombok.Getter;
import pl.blackwater.mysqlsecurity.data.HttpResponse;

@Getter

public class AuthException extends RuntimeException{

    private final HttpResponse httpResponse;


    public AuthException(HttpResponse httpResponse) {
        super(httpResponse.getMessage());
        this.httpResponse = httpResponse;
    }
}
