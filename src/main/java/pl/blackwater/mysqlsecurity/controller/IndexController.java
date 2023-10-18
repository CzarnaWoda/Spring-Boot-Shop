package pl.blackwater.mysqlsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.blackwater.mysqlsecurity.data.HttpResponse;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("")
public class IndexController {

    @GetMapping("/index")
    @PreAuthorize("hasRole('ROLE_USER')")
    public HttpResponse index() {
        return HttpResponse.builder().statusCode(OK.value())
                .status(OK)
                .message("ITS OKAY").build();
    }
    @GetMapping("login")
    public String login(){
        return "login";
    }
}
