package pl.blackwater.mysqlsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.blackwater.mysqlsecurity.model.Role;
import pl.blackwater.mysqlsecurity.model.User;
import pl.blackwater.mysqlsecurity.repository.UserRepository;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("User cant be found in repository");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isNotLocked(),
                true,
                user.isNotLocked(),
                getAuthorities(user.getRoles()));
    }
    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(userRole -> new SimpleGrantedAuthority( userRole.getName()))
                .collect(Collectors.toList());
    }
}
