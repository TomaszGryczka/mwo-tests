package com.github.tomaszgryczka.mwotests2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ProductServiceTests {
    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @MockBean
    private UserService userService;

    @Test
    public void should_OrderProduct_When_ProductIsAvailable() {
        // given
        final String productId = "1";
        final String accountId = "1";
        final double accountBalance = 1000;
        final Product availableProduct = Product.builder()
                .id("1")
                .name("IPHONE MILION")
                .price(900)
                .isAvailable(true)
                .build();

        Mockito.when(productRepository.findProductById(productId)).thenReturn(availableProduct);
        Mockito.when(userService.checkAccountBalance(accountId)).thenReturn(accountBalance);

        // when
        final boolean result = productService.orderProduct(accountId, productId);

        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void should_ReturnFalse_When_ProductIsUnavailable() {
        // given
        final String productId = "1";
        final String accountId = "1";
        final double accountBalance = 1000;
        final Product unavailableProduct = Product.builder()
                .id("1")
                .name("IPHONE MILION")
                .price(900)
                .isAvailable(false)
                .build();

        Mockito.when(productRepository.findProductById(productId)).thenReturn(unavailableProduct);
        Mockito.when(userService.checkAccountBalance(accountId)).thenReturn(accountBalance);

        // when
        final boolean result = productService.orderProduct(accountId, productId);

        // then
        Assertions.assertFalse(result);
    }

    @Test
    public void should_ThrowsIllegalStateException_When_AccountBalanceIsInsufficient() {
        // given
        final String productId = "1";
        final String accountId = "1";
        final double accountBalance = 100;
        final Product availableProduct = Product.builder()
                .id("1")
                .name("IPHONE MILION")
                .price(900)
                .isAvailable(true)
                .build();

        Mockito.when(productRepository.findProductById(productId)).thenReturn(availableProduct);
        Mockito.when(userService.checkAccountBalance(accountId)).thenReturn(accountBalance);

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> productService.orderProduct(accountId, productId));
    }

    @Test
    public void should_ReturnProductPrice_When_ProductExists() {
        // given
        final String productId = "1";
        final double productPrice = 900;
        final Product product = Product.builder()
                .id("1")
                .name("IPHONE MILION")
                .price(productPrice)
                .isAvailable(true)
                .build();

        Mockito.when(productRepository.findProductById(productId)).thenReturn(product);

        // when
        double resultPrice = productService.checkProductPrice(productId);

        // then
        Assertions.assertEquals(productPrice, resultPrice);
    }

    @Test
    public void should_ThrowException_When_ProductDoesNotExist() {
        // given
        final String productId = "1";

        Mockito.when(productRepository.findProductById(productId)).thenReturn(null);

        // when
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.checkProductPrice(productId));
    }
}
