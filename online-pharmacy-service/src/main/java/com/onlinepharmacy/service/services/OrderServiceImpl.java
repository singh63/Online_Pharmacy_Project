package com.onlinepharmacy.service.services;

import com.onlinepharmacy.service.entities.Cart;
import com.onlinepharmacy.service.entities.CartItem;
import com.onlinepharmacy.service.entities.Order;
import com.onlinepharmacy.service.entities.OrderItem;
import com.onlinepharmacy.service.entities.Payment;
import com.onlinepharmacy.service.entities.Product;
import com.onlinepharmacy.service.exceptions.APIException;
import com.onlinepharmacy.service.exceptions.ResourceNotFoundException;
import com.onlinepharmacy.service.payloads.OrderDTO;
import com.onlinepharmacy.service.payloads.OrderItemDTO;
import com.onlinepharmacy.service.payloads.OrderResponse;
import com.onlinepharmacy.service.repositories.CartRepo;
import com.onlinepharmacy.service.repositories.OrderItemRepo;
import com.onlinepharmacy.service.repositories.OrderRepo;
import com.onlinepharmacy.service.repositories.PaymentRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {

    private final CartRepo cartRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final PaymentRepo paymentRepo;

    @Override
    public OrderDTO placeOrder(String emailId, Long cartId, String paymentMethod) {
//		Retrieves the cart and validates its existence.
//		Creates and saves an order along with payment information.
//		Processes cart items to create corresponding OrderItem objects.
//		Updates product quantities and removes items from the cart.
//		Returns an OrderDTO with all the details of the placed order and its items.
        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        Order order = new Order();

        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());

        order.setTotalAmount(cart.getCartItems()
                .stream()
                .map(CartItem::getProduct)
                .map(Product::getPrice)
                .mapToDouble(price -> price)
                .sum());
        order.setOrderStatus("PLACED");

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);

        payment = paymentRepo.save(payment);

        order.setPayment(payment);

        Order savedOrder = orderRepo.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(savedOrder);
            orderItem.setOrderedProductPrice(cartItem.getProduct().getPrice());
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepo.saveAll(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();

            Product product = item.getProduct();

            cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());

            product.setQuantity(product.getQuantity() - quantity);
        });

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        return orderDTO;
    }

    @Override
    public OrderDTO getOrder(String emailId, Long orderId) {
//		Retrieves a specific order for a user based on emailId and orderId.
//		Throws an exception if the order is not found.
//		Returns the OrderDTO representation of the order if it exists.
        Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);

        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderId", orderId);
        }

        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(String emailId) {
//		Retrieves all orders for a user based on their email ID.
//		Maps the Order entities to OrderDTO objects.
//		Throws an exception if no orders are found.
//		Returns the list of OrderDTO objects if orders are present.
        List<Order> orders = orderRepo.findAllByEmail(emailId);

        List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());

        if (orderDTOs.isEmpty()) {
            throw new APIException("No orders placed yet by the user with email: " + emailId);
        }

        return orderDTOs;
    }

    @Override
    public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//		Retrieves all orders with pagination and sorting.
//		Converts the orders to DTOs for response.
//		Throws an exception if no orders are found.
//		Returns the paginated list of orders with detailed metadata.
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Order> pageOrders = orderRepo.findAll(pageDetails);

        List<Order> orders = pageOrders.getContent();

        List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());

        if (orderDTOs.isEmpty()) {
            throw new APIException("No orders placed yet by the users");
        }

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());

        return orderResponse;
    }

    @Override
    public OrderDTO updateOrder(String emailId, Long orderId, String orderStatus) {
//		Retrieves an order based on emailId and orderId.
//		Throws an exception if the order does not exist.
//		Updates the status of the order.
//		Returns the updated order as a DTO.
        Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);

        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderId", orderId);
        }

        order.setOrderStatus(orderStatus);
        return modelMapper.map(order, OrderDTO.class);
    }
}
