package com.example.activity3.Security.Service;

import com.example.activity3.Security.Repo.AppRoleRepository;
import com.example.activity3.Security.Repo.AppUserRepository;
import com.example.activity3.Security.entities.AppRole;
import com.example.activity3.Security.entities.AppUser;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public AppUser AddNewUser(AppUser appUser, String confirmPassword) {
        if (loadUserByUsername(appUser.getUsername())!=null)
           throw new RuntimeException("Username already exists");
        if (!appUser.getPassword().equals(confirmPassword))
            throw new RuntimeException("Password does not match");
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUser loadUserByEmail(String email) {
        return null;
    }

    @Override
    public AppUser DeleteUserByUsername(String username) {
        return null;
    }

    @Override
    public AppUser DeleteUserByEmail(String email) {
        return null;
    }

    @Override
    public AppRole AddNewRole(AppRole appRole) {
        if (appRoleRepository.findById(appRole.getRoleId()).isPresent())
            throw new RuntimeException("Role already exists");
        return appRoleRepository.save(appRole);
    }

    @Override
    public void AddUserRole(String username, String roleName) {
        AppUser appUser =loadUserByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        if (appUser==null)
            throw new RuntimeException("Username doesn't exists");
        if (appRole == null)
            throw new RuntimeException("Role doesn't exists");
        appUser.getAppRoles().add(appRole);
        appRoleRepository.save(appRole);
    }

    @Override
    public void DeleteUserRole(String username, String roleName) {
        AppUser user = appUserRepository.findByUsername(username);
        if (user==null)
            throw new RuntimeException("Username doesn't exists");
        AppRole appRole = appRoleRepository.findById(roleName).orElse(null);
        if (appRole==null)
            throw new RuntimeException("Role doesn't exists");
        user.getAppRoles().remove(appRole);
        appRoleRepository.save(appRole);
    }
}
