package com.onlinepharmacy.service.services;

import com.onlinepharmacy.service.payloads.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long cartId, Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    void updateProductInCarts(Long cartId, Long productId);

    void deleteProductFromCart(Long cartId, Long productId);
}
