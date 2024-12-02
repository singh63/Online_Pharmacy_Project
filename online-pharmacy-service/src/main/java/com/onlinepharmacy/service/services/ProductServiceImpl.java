package com.onlinepharmacy.service.services;

import com.onlinepharmacy.service.entities.Cart;
import com.onlinepharmacy.service.entities.Category;
import com.onlinepharmacy.service.entities.Product;
import com.onlinepharmacy.service.exceptions.APIException;
import com.onlinepharmacy.service.exceptions.ResourceNotFoundException;
import com.onlinepharmacy.service.payloads.CartDTO;
import com.onlinepharmacy.service.payloads.ProductDTO;
import com.onlinepharmacy.service.payloads.ProductResponse;
import com.onlinepharmacy.service.repositories.CartRepo;
import com.onlinepharmacy.service.repositories.CategoryRepo;
import com.onlinepharmacy.service.repositories.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final CartRepo cartRepo;
    private final CartService cartService;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {
//		Retrieves the category by ID.
//		Checks if a product with the same name and description already exists in the category.
//		If the product does not exist, sets a default image, associates it with the category, calculates the special price, and saves the product.
//		Returns the saved product as a ProductDTO.
//		If the product already exists, throws an exception.
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();

        for (Product value : products) {
            if (value.getProductName().equals(product.getProductName())
                    && value.getDescription().equals(product.getDescription())) {
                isProductNotPresent = false;
                break;
            }
        }

        if (isProductNotPresent) {
            product.setImage("default.png");

            product.setCategory(category);

            Product savedProduct = productRepo.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new APIException("Product already exists !!!");
        }
    }

    @Override
    public ProductDTO getProduct(long productId) {
        Product product = productRepo.findById(productId).orElseThrow();
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//		Determines the sort order based on the input parameters.
//		Defines paging details.
//		Retrieves a paginated list of products from the repository.
//		Maps product entities to DTOs.
//		Populates and returns a ProductResponse with product data and pagination details.
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepo.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                                            String sortOrder) {
//		Retrieves the category by ID.
//		Determines the sort order based on the input parameters.
//		Defines paging details.
//		Retrieves a paginated list of products from the repository.
//		Checks if the category contains any products and throws an exception if not.
//		Maps product entities to DTOs.
//		Populates and returns a ProductResponse with product data and pagination details.
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepo.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();

        if (products.isEmpty()) {
            throw new APIException(category.getCategoryName() + " category doesn't contain any products !!!");
        }

        List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, Product product) {
//		Retrieves the existing product by ID and throws an exception if not found.
//		Sets necessary fields from the existing product to the updated product.
//		Saves the updated product in the repository.
//		Retrieves and maps carts containing the updated product to DTOs.
//		Updates the product in each cart.
//		Returns the updated product as a DTO.
        Product productFromDB = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (productFromDB == null) {
            throw new APIException("Product not found with productId: " + productId);
        }

        product.setImage(productFromDB.getImage());
        product.setProductId(productId);
        product.setCategory(productFromDB.getCategory());

        Product savedProduct = productRepo.save(product);

        List<Cart> carts = cartRepo.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).toList();

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
//		Retrieves the product by ID and handles the case where it is not found.
//		Uploads the new image using the fileService and updates the product's image field.
//		Saves the updated product and returns it as a ProductDTO.
        Product productFromDB = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (productFromDB == null) {
            throw new APIException("Product not found with productId: " + productId);
        }

        String fileName = fileService.uploadImage(path, image);

        productFromDB.setImage(fileName);

        Product updatedProduct = productRepo.save(productFromDB);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//		Determines the sort order based on input parameters.
//		Defines paging details.
//		Retrieves a paginated list of products matching the keyword from the repository.
//		Checks if any products match the keyword and throws an exception if not.
//		Maps product entities to DTOs.
//		Populates and returns a ProductResponse with product data and pagination details

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        String wrappedKeyword = "%" + keyword + "%";
        Page<Product> pageProducts = productRepo.findByProductNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(
                wrappedKeyword, "", pageDetails);

        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public String deleteProduct(Long productId) {
//		The method retrieves the product by its ID and ensures it exists.
//		Finds all carts containing the product and removes the product from each cart.
//		Deletes the product from the repository and returns a confirmation message.
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        List<Cart> carts = cartRepo.findCartsByProductId(productId);

        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepo.delete(product);

        return "Product with productId: " + productId + " deleted successfully !!!";
    }
}
