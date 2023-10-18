package pl.blackwater.mysqlsecurity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.blackwater.mysqlsecurity.config.AccountAuthenticationProvider;
import pl.blackwater.mysqlsecurity.data.AuthRequest;
import pl.blackwater.mysqlsecurity.data.HttpResponse;
import pl.blackwater.mysqlsecurity.data.LoginRequest;
import pl.blackwater.mysqlsecurity.dto.UserDTO;
import pl.blackwater.mysqlsecurity.dto.UserDTOMapper;
import pl.blackwater.mysqlsecurity.exception.AuthException;
import pl.blackwater.mysqlsecurity.jwt.TokenService;
import pl.blackwater.mysqlsecurity.repository.UserRepository;
import pl.blackwater.mysqlsecurity.service.UserService;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    private final TokenService tokenService;
    private final AccountAuthenticationProvider authenticationManager;

    @PostMapping("/register")
    @Valid
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid AuthRequest authRequest, Errors error){
        if(error.hasErrors()){
            return ResponseEntity.created(getUri()).body(HttpResponse.builder()
                    .timeStamp(now().toString())
                    .message(Objects.requireNonNull(error.getFieldError()).getDefaultMessage())
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .build()
            );
        }
        if(!userRepository.existsUserByEmail(authRequest.getEmail())){
            if(!userRepository.existsUserByFirstName(authRequest.getFirstName())){

                final UserDTO userDTO = userService.createUser(authRequest.getFirstName(),authRequest.getLastName(),authRequest.getEmail(),authRequest.getPassword(),authRequest.getPhone());

                return ResponseEntity.created(getUri()).body(HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(Map.of("user",userDTO))
                                .message("User created")
                                .status(CREATED)
                                .statusCode(CREATED.value())
                        .build()
                );
            }else{
                return ResponseEntity.created(getUri()).body(HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("authRequest",authRequest))
                        .message("User not created, user of that name already exist")
                        .status(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .build()
                );
            }
        }else{
            return ResponseEntity.created(getUri()).body(HttpResponse.builder()
                    .timeStamp(now().toString())
                    .data(Map.of("authRequest",authRequest))
                    .message("User not created, user of that email already exist")
                    .status(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .build()
            );
        }
    }
    @PostMapping("/login")
    @CrossOrigin("*")
    public ResponseEntity<HttpResponse> login(@RequestBody LoginRequest loginRequest) throws AuthException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            String token = tokenService.generateToken(authentication);
            final UserDTO userDTO = UserDTOMapper.fromUser(userRepository.findByEmail(loginRequest.getEmail()));

            return ResponseEntity.accepted().body(HttpResponse.builder()
                    .statusCode(ACCEPTED.value())
                    .status(ACCEPTED)
                    .message(token)
                    .timeStamp(now().toString())
                    .data(Map.of("user",userDTO)).build());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(HttpResponse
                    .builder()
                    .message("Wrong login or password")
                    .timeStamp(String.valueOf(now()))
                    .statusCode(UNAUTHORIZED.value())
                    .status(UNAUTHORIZED)
                    .data(Map.of("AuthenticationException",e.getMessage()))
                    .developerMessage("TEST 1").build());
        }catch (AuthException e){
            return ResponseEntity.badRequest().body(e.getHttpResponse());
        }
    }
    @GetMapping("/details")
    @CrossOrigin("*")
    public ResponseEntity<HttpResponse> details(Authentication authentication){
        if(userRepository.existsUserByEmail(authentication.getName())) {
            final UserDTO userDTO = UserDTOMapper.fromUser(userRepository.findByEmail(authentication.getName()));


            return ResponseEntity.accepted().body(HttpResponse.builder()
                    .data(Map.of("User",userDTO))
                    .status(ACCEPTED)
                    .statusCode(ACCEPTED.value())
                    .message("User Informations")
                    .timeStamp(now().toString()).build());
        }

        return ResponseEntity.status(NOT_FOUND).body(HttpResponse.builder()
                .data(Map.of("Auth",authentication))
                .status(NOT_FOUND)
                .statusCode(NOT_FOUND.value())
                .message("User not found by email")
                .timeStamp(now().toString()).build());
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
}
