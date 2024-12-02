package com.onlinepharmacy.service;

import com.onlinepharmacy.service.services.DbInitializerService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@SecurityScheme(
        name = "E-Commerce Application",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OnlinePharmacyServiceApplication {

    private final DbInitializerService dbInitializerService;

    public static void main(String[] args) {
        SpringApplication.run(OnlinePharmacyServiceApplication.class, args);
    }

    @PostConstruct
    public void initializeDatabase() {
        dbInitializerService.run();
    }
}
