package pl.blackwater.mysqlsecurity.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.blackwater.mysqlsecurity.service.AuthUserDetailService;

@Component
@RequiredArgsConstructor
public class AccountAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final AuthUserDetailService authUserDetailService;
    private final PasswordEncoder passwordEncoder;


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        final String password = userDetails.getPassword();
        final String credentials = (String) authentication.getCredentials();
        if(credentials == null || password == null){
            throw new BadCredentialsException("Cant be null");
        }
        if(!passwordEncoder.matches(credentials,password)){
            throw new BadCredentialsException("Invalid password");
        }

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return authUserDetailService.loadUserByUsername(username);
    }
}
