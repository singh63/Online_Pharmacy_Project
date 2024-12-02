package com.onlinepharmacy.service.repositories;

import com.onlinepharmacy.service.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    Category findByCategoryName(String categoryName);
}
