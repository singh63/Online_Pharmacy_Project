package com.onlinepharmacy.service.services;

import com.onlinepharmacy.service.entities.Product;
import com.onlinepharmacy.service.payloads.ProductDTO;
import com.onlinepharmacy.service.payloads.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);

    ProductDTO getProduct(long productId);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                                     String sortOrder);

    ProductDTO updateProduct(Long productId, Product product);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    String deleteProduct(Long productId);
}
