package com.onlinepharmacy.service.services;

import com.onlinepharmacy.service.entities.User;
import com.onlinepharmacy.service.exceptions.ResourceNotFoundException;
import com.onlinepharmacy.service.payloads.UserInfoConfig;
import com.onlinepharmacy.service.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OnlinePharmacyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(username);
        return user.map(UserInfoConfig::new).orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
    }
}
