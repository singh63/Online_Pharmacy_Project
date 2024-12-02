package com.onlinepharmacy.service.services;

import com.onlinepharmacy.service.config.AppConstants;
import com.onlinepharmacy.service.entities.Cart;
import com.onlinepharmacy.service.entities.CartItem;
import com.onlinepharmacy.service.entities.Role;
import com.onlinepharmacy.service.entities.User;
import com.onlinepharmacy.service.exceptions.APIException;
import com.onlinepharmacy.service.exceptions.ResourceNotFoundException;
import com.onlinepharmacy.service.payloads.AddressDTO;
import com.onlinepharmacy.service.payloads.CartDTO;
import com.onlinepharmacy.service.payloads.ProductDTO;
import com.onlinepharmacy.service.payloads.UserDTO;
import com.onlinepharmacy.service.payloads.UserResponse;
import com.onlinepharmacy.service.repositories.RoleRepo;
import com.onlinepharmacy.service.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        try {
            User user = modelMapper.map(userDTO, User.class);
            Role role = roleRepo.findById(AppConstants.USER_ID).orElseThrow();
            user.getRoles().add(role);

            Cart cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);

            User registeredUser = userRepo.save(user);
            userDTO = modelMapper.map(registeredUser, UserDTO.class);
            return userDTO;
        } catch (DataIntegrityViolationException e) {
            throw new APIException("User already exists with emailId: " + userDTO.getEmail());
        }
    }

    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//		Sorting and Pagination: Configured using Sort and Pageable.
//		User Retrieval: Paginated and sorted list of users fetched from the repository.
//		Exception Handling: Throws an exception if no users are found.
//		DTO Mapping: Converts User entities to UserDTO, including address and cart details.
//		Response Construction: Builds and returns a UserResponse with pagination and user data.
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<User> pageUsers = userRepo.findAll(pageDetails);

        List<User> users = pageUsers.getContent();

        if (users.isEmpty()) {
            throw new APIException("No User exists !!!");
        }

        List<UserDTO> userDTOs = users.stream().map(user -> {
            UserDTO dto = modelMapper.map(user, UserDTO.class);

            if (!user.getAddresses().isEmpty()) {
                dto.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
            }

            CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

            List<ProductDTO> products = user.getCart().getCartItems().stream()
                    .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            dto.setCart(cart);

            dto.getCart().setProducts(products);

            return dto;

        }).collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();

        userResponse.setContent(userDTOs);
        userResponse.setPageNumber(pageUsers.getNumber());
        userResponse.setPageSize(pageUsers.getSize());
        userResponse.setTotalElements(pageUsers.getTotalElements());
        userResponse.setTotalPages(pageUsers.getTotalPages());
        userResponse.setLastPage(pageUsers.isLast());

        return userResponse;
    }

    @Override
    public UserDTO getUserById(Long userId) {
//		User Retrieval: Gets the user from the database by ID.
//		DTO Mapping: Converts User entity to UserDTO and includes the address and cart information.
//		Address Mapping: Sets the first address from the user's address list.
//		Cart and Products: Maps the cart and its products, and sets them in the DTO.
//		Return: Provides the complete UserDTO with all associated details.
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        user.getAddresses().stream().findFirst().ifPresent(address ->
                userDTO.setAddress(modelMapper.map(address, AddressDTO.class)));

        CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

        List<ProductDTO> products = user.getCart().getCartItems().stream()
                .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

        userDTO.setCart(cart);

        userDTO.getCart().setProducts(products);

        return userDTO;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
//		User Retrieval: Retrieves the user by ID from the repository.
//		Password Encoding: Encodes the new password.
//		User Update: Updates the user's details with the new values.
//		Address Handling: Updates or creates a new address if provided.
//		DTO Mapping: Maps the updated user to UserDTO and includes address and cart details.
//		Return: Provides the updated UserDTO with all changes.
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        String encodedPass = passwordEncoder.encode(userDTO.getPassword());

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encodedPass);

        userDTO = modelMapper.map(user, UserDTO.class);

        CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

        List<ProductDTO> products = user.getCart().getCartItems().stream()
                .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

        userDTO.setCart(cart);

        userDTO.getCart().setProducts(products);

        return userDTO;
    }

    @Override
    public String deleteUser(Long userId) {
//		User Retrieval: Retrieves the user by ID.
//		Cart Item Processing: Deletes all products from the user's cart.
//		User Deletion: Removes the user from the repository.
//		Return Confirmation: Provides a confirmation message indicating successful deletion.
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        List<CartItem> cartItems = user.getCart().getCartItems();
        Long cartId = user.getCart().getCartId();

        cartItems.forEach(item -> {

            Long productId = item.getProduct().getProductId();

            cartService.deleteProductFromCart(cartId, productId);
        });

        userRepo.delete(user);
        return "User with userId " + userId + " deleted successfully!!!";
    }
}
