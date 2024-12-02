package com.onlinepharmacy.service.repositories;

import com.onlinepharmacy.service.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

}
