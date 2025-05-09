package com.example.activity3.Security.Service;

import com.example.activity3.Security.entities.AppRole;
import com.example.activity3.Security.entities.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException(String.format("User %s not found",username));
        }
        String[] roles = appUser.getAppRoles().stream().map(AppRole::getRoleName).toArray(String[]::new);
        return User.withUsername(appUser.getUsername()).password(appUser.getPassword()).roles(roles).build();
    }
}
