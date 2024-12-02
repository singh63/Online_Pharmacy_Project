package com.onlinepharmacy.service.repositories;

import com.onlinepharmacy.service.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Page<Product> findByProductNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(String name, String description, Pageable pagedetails);
}
