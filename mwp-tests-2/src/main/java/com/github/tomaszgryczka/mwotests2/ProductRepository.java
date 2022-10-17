package com.github.tomaszgryczka.mwotests2;

import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
    public Product findProductById(final String id) {
        // ...
        return null;
    }

    public void makeProductAsUnavailable(final String id) {
        // ...
    }
}
