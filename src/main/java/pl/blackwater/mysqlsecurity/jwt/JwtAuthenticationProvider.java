package pl.blackwater.mysqlsecurity.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.blackwater.mysqlsecurity.service.AuthUserDetailService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final AuthUserDetailService authUserDetailService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();

        if(authentication.getDetails().equals("/index")){
            return null;
        }
        final UserDetails userDetails = authUserDetailService.loadUserByUsername(email);


        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw  new BadCredentialsException("Invalid password");
        }
        System.out.println("USER DEATILS AUTHO: " + userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(email,password,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
