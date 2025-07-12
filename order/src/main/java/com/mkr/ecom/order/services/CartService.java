package com.mkr.ecom.order.services;

import com.mkr.ecom.order.clients.ProductServiceClient;
import com.mkr.ecom.order.dto.CartItemRequest;
import com.mkr.ecom.order.dto.ProductResponse;
import com.mkr.ecom.order.models.CartItem;
import com.mkr.ecom.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

//    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
//    private final UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
        // Look for product
//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//        if(productOpt.isEmpty()) {
//            return false;
//        }
        ProductResponse productResponse = productServiceClient.getProductDetails(request.getProductId());
        if (productResponse == null || productResponse.getStockQuantity() < request.getQuantity())
            return false;

//        Product product = productOpt.get();
//        if (product.getStockQuantity() < request.getQuantity()) {
//            return false;
//        }
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty()) {
//            return false;
//        }
//
//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if(existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
//            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            existingCartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
//            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId) {
//        Optional<Product> productOpt = productRepository.findById(productId);
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
