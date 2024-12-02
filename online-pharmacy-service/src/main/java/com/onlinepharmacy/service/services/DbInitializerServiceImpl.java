package com.onlinepharmacy.service.services;

import com.onlinepharmacy.service.config.AppConstants;
import com.onlinepharmacy.service.entities.Cart;
import com.onlinepharmacy.service.entities.Role;
import com.onlinepharmacy.service.entities.User;
import com.onlinepharmacy.service.repositories.RoleRepo;
import com.onlinepharmacy.service.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConfigurationProperties(prefix = "online-pharmacy", ignoreInvalidFields = true)
@Transactional
public class DbInitializerServiceImpl implements DbInitializerService {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    @Setter
    private Map<String, String> adminConfig;

    @Override
    public void run() {
        if (roleRepo.count() == 0) {
            Role adminRole = new Role(AppConstants.ADMIN_ID, "ADMIN");
            Role userRole = new Role(AppConstants.USER_ID, "USER");
            List<Role> roles = List.of(adminRole, userRole);
            roleRepo.saveAll(roles);
        }

        if (userRepo.findByEmail(adminConfig.get("email")).isEmpty()) {
            User user = new User();
            user.setFirstName(adminConfig.get("name"));
            user.setLastName(adminConfig.get("name"));
            user.setMobileNumber(adminConfig.get("mobile"));
            user.setEmail(adminConfig.get("email"));
            user.setPassword(passwordEncoder.encode(adminConfig.get("password")));

            Role userRole = roleRepo.findById(AppConstants.USER_ID).orElseThrow();
            Role adminRole = roleRepo.findById(AppConstants.ADMIN_ID).orElseThrow();
            user.getRoles().add(userRole);
            user.getRoles().add(adminRole);

            user.setCart(new Cart());
            user.getCart().setUser(user);

            userRepo.save(user);
        }
    }
}
