package com.onlinepharmacy.service.controllers;

import com.onlinepharmacy.service.config.AppConstants;
import com.onlinepharmacy.service.payloads.OrderDTO;
import com.onlinepharmacy.service.payloads.OrderResponse;
import com.onlinepharmacy.service.services.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/users/{emailId}/carts/{cartId}/payments/{paymentMethod}/order")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String emailId, @PathVariable Long cartId, @PathVariable String paymentMethod) {
        OrderDTO order = orderService.placeOrder(emailId, cartId, paymentMethod);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("/users/{emailId}/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String emailId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(emailId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/users/{emailId}/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrderByUser(@PathVariable String emailId, @PathVariable Long orderId) {
        OrderDTO order = orderService.getOrder(emailId, orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/admin/users/{emailId}/orders/{orderId}/orderStatus/{orderStatus}")
    public ResponseEntity<OrderDTO> updateOrderByUser(@PathVariable String emailId, @PathVariable Long orderId, @PathVariable String orderStatus) {
        OrderDTO order = orderService.updateOrder(emailId, orderId, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
