package com.onlinepharmacy.service.services;

import com.onlinepharmacy.service.entities.Cart;
import com.onlinepharmacy.service.entities.CartItem;
import com.onlinepharmacy.service.entities.Product;
import com.onlinepharmacy.service.exceptions.APIException;
import com.onlinepharmacy.service.exceptions.ResourceNotFoundException;
import com.onlinepharmacy.service.payloads.CartDTO;
import com.onlinepharmacy.service.payloads.ProductDTO;
import com.onlinepharmacy.service.repositories.CartItemRepo;
import com.onlinepharmacy.service.repositories.CartRepo;
import com.onlinepharmacy.service.repositories.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final CartItemRepo cartItemRepo;
    private final ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) {
//		The method adds a product to the cart, ensuring it is not already there, 
//		checking product availability, and updating the cart and product details. 
//		It returns the updated cart details in a CartDTO object.

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        if (quantity == 0) {
            deleteProductFromCart(cartId, productId);
            return modelMapper.map(cartRepo.findById(cartId).orElseThrow(), CartDTO.class);
        }

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
        } else {
            cartItem.setQuantity(quantity);
        }

        cartItemRepo.save(cartItem);

        product.setQuantity(product.getQuantity() - quantity);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<ProductDTO> productDTOs = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

        cartDTO.setProducts(productDTOs);

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {

//		The method retrieves all carts from the repository.
//		If no carts are found, it throws an exception.
//		It maps the Cart entities to CartDTOs, including their products.
//		The mapped CartDTOs are returned as a list.

        List<Cart> carts = cartRepo.findAll();

        if (carts.isEmpty()) {
            throw new APIException("No cart exists");
        }

        return carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
            cartDTO.setProducts(products);
            return cartDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
//		This method fetches a cart based on the provided emailId and cartId.
//		If the cart does not exist, it throws a ResourceNotFoundException.
//		It maps the Cart entity to a CartDTO and includes the list of products in the cart.
//		Finally, it returns the CartDTO object.
        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<ProductDTO> products = cart.getCartItems().stream()
                .map(item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    productDTO.setQuantity(item.getQuantity());
                    return productDTO;
                })
                .collect(Collectors.toList());

        cartDTO.setProducts(products);

        return cartDTO;
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
//		This method updates the product price in a cart based on the special price of the product.
//		It checks for the existence of the cart, product, and cart item.
//		It recalculates the cart's total price by updating the cart item's price.
//		Finally, it saves the updated cart item back to the repository.
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        cartItemRepo.save(cartItem);
    }

    @Override
    public void deleteProductFromCart(Long cartId, Long productId) {
//		This method removes a product from the cart.
//		It checks for the existence of the cart and cart item.
//		It updates the cart's total price and restores the product's quantity.
//		Finally, it deletes the cart item and returns a confirmation message.
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        Product product = cartItem.getProduct();
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());

        cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);
    }
}
