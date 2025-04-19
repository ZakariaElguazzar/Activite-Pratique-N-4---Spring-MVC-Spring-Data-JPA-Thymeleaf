package com.example.activity3.Security.Service;

import com.example.activity3.Security.entities.AppRole;
import com.example.activity3.Security.entities.AppUser;

public interface AccountService {
    AppUser AddNewUser(AppUser appUser, String ConfirmPassword);
    AppUser loadUserByUsername(String username);
    AppUser loadUserByEmail(String email);
    AppUser DeleteUserByUsername(String username);
    AppUser DeleteUserByEmail(String email);
    AppRole AddNewRole(AppRole appRole);
    void AddUserRole(String username,String roleName);
    void DeleteUserRole(String username,String roleName);
}
