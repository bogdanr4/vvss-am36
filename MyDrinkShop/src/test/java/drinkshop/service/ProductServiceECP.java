package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductServiceECP {

    private static final int productId = 1;
    private static Repository<Integer, Product> productRepo;
    private ProductService productService;
    @BeforeAll
    static void beforeAll() {
        productRepo = new AbstractRepository<>() {
            @Override
            protected Integer getId(Product entity) {
                return entity.getId();
            }
        };
    }

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepo, new ProductValidator());
    }

    @AfterEach
    void tearDown() {
        productRepo.delete(productId);
        productService = null;
    }

    @Test
    void addProduct_tc1_valid() {
        //Arrange
        Product product= new Product(productId, "Flat White", 1, "MILK_COFFEE", "DAIRY");
        //Act
        productService.addProduct(product);
        Product result = productService.findById(productId);
        //Assert
        assertEquals(product, result);
    }

    @Test
    void addProduct_tc2_negativePrice() {
        //Arrange
        Product product= new Product(productId, "Flat White", -1, "MILK_COFFEE", "DAIRY");

        //Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.addProduct(product));

        //Assert
        assertEquals("Negative price!", exception.getMessage());

    }

    @Test
    void addProduct_tc3_invalidName() {
        //Arrange
        Product product= new Product(productId, "", 1, "MILK_COFFEE", "DAIRY");

        //Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.addProduct(product));

        //Assert
        assertEquals("Invalid Name!", exception.getMessage());

    }

    @Test
    void addProduct_tc6_invalidName() {
        //Arrange
        String name = "A".repeat(300);
        Product product= new Product(productId, name, 1, "MILK_COFFEE", "DAIRY");

        //Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.addProduct(product));

        //Assert
        assertEquals("Name too long!", exception.getMessage());
    }


}
