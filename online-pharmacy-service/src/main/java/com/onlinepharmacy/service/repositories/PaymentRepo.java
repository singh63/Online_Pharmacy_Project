package com.onlinepharmacy.service.repositories;

import com.onlinepharmacy.service.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {

}
