package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ProductService BVA tests for addProduct")
@Tag("BVA")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceBVA {

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
    @Order(1)
    @DisplayName("BVA valid: name length at invalid minimum boundary (0)")
    void addProduct_tc1_invalid() {
        //Arrange
        Product product= new Product(productId, "", 7, "MILK_COFFEE", "DAIRY");
        //Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.addProduct(product));
        //Assert
        assertEquals("Invalid Name!", exception.getMessage());
    }

    @Test
    @Order(2)
    @DisplayName("BVA valid: name length at minimum boundary (1)")
    void addProduct_tc3_valid() {
        //Arrange
        Product product= new Product(productId, "M", 7, "MILK_COFFEE", "DAIRY");
        //Act
        productService.addProduct(product);
        Product result = productService.findById(productId);
        //Assert
        assertEquals(product, result);
    }

    @Test
    @Order(3)
    @DisplayName("BVA valid: name length at maximum boundary (255)")
    void addProduct_tc4_valid() {
        //Arrange
        String name = "M".repeat(255);
        Product product= new Product(productId, name, 1, "MILK_COFFEE", "DAIRY");
        //Act
        productService.addProduct(product);
        Product result = productService.findById(productId);
        //Assert
        assertEquals(product, result);
    }

    @Test
    @Order(4)
    @DisplayName("BVA valid: name length at max-1 boundary (254)")
    void addProduct_tc5_valid() {
        //Arrange
        String name = "M".repeat(254);
        Product product= new Product(productId, name, 1, "MILK_COFFEE", "DAIRY");
        //Act
        productService.addProduct(product);
        Product result = productService.findById(productId);
        //Assert
        assertEquals(product, result);
    }

    @Test
    @Order(5)
    @DisplayName("BVA invalid: name length at max+1 boundary (256)")
    void addProduct_tc6_invalidName() {
        //Arrange
        String name = "M".repeat(256);
        Product product= new Product(productId, name, 1, "MILK_COFFEE", "DAIRY");
        //Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.addProduct(product));
        //Assert
        assertEquals("Name too long!", exception.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("BVA invalid: price at invalid boundary (-1)")
    void addProduct_tc7_invalidPrice() {
        //Arrange
        Product product= new Product(productId, "Flat White", -1, "MILK_COFFEE", "DAIRY");

        //Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.addProduct(product));

        //Assert
        assertEquals("Negative price!", exception.getMessage());

    }

    @Test
    @Order(7)
    @DisplayName("BVA invalid: price at invalid boundary (0)")
    void addProduct_tc8_invalidPrice() {
        //Arrange
        Product product= new Product(productId, "Flat White", 0, "MILK_COFFEE", "DAIRY");

        //Act
        ValidationException exception = assertThrows(ValidationException.class, () -> productService.addProduct(product));

        //Assert
        assertEquals("Null price!", exception.getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("BVA valid: price at minimum positive boundary (1)")
    void addProduct_tc9_valid() {
        //Arrange
        Product product= new Product(productId, "Flat White", 1, "MILK_COFFEE", "DAIRY");
        //Act
        productService.addProduct(product);
        Product result = productService.findById(productId);
        //Assert
        assertEquals(product, result);
    }


}
