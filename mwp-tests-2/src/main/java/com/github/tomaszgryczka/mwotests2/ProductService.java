package com.github.tomaszgryczka.mwotests2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserService userService;
    private final ProductRepository productRepository;

    public boolean orderProduct(final String accountId, final String productId) {
        final Product product = productRepository.findProductById(productId);
        final boolean isAvailable = product.isAvailable();

        if (isAvailable) {
            final double accountBalance = userService.checkAccountBalance(accountId);

            if (product.getPrice() < accountBalance) {
                productRepository.makeProductAsUnavailable(productId);
                return true;
            } else {
                throw new IllegalStateException("Insufficient funds to buy product.");
            }
        }
        return false;
    }

    public double checkProductPrice(final String productId) {
        return Optional.ofNullable(productRepository.findProductById(productId))
                .map(Product::getPrice)
                .orElseThrow(() -> new IllegalArgumentException("Product with given id does not exist"));
    }
}
