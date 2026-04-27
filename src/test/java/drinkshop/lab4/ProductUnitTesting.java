package drinkshop.lab4;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductUnitTesting {

    private Repository<Integer, Product> productRepo;


    private Validator<Product> validator;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepo = mock(Repository.class);
        validator = mock(Validator.class);
        productService = new ProductService(productRepo, validator);
    }
    @Test
    void addProduct_validProduct() {
        Product product = new Product(1, "Flat White", 1, "MILK_COFFEE", "DAIRY");

        productService.addProduct(product);

        verify(validator, times(1)).validate(product);
        verify(productRepo, times(1)).save(product);
    }

    @Test
    void addProduct_invalidProduct() {
        Product product =new Product(0, "", 1, "MILK_COFFEE", "DAIRY");;

        doThrow(new ValidationException("ID invalid!Invalid Name!"))
                .when(validator)
                .validate(product);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> productService.addProduct(product)
        );

        assertEquals("ID invalid!Invalid Name!", exception.getMessage());

        verify(validator, times(1)).validate(product);
        verify(productRepo, never()).save(product);
    }

    @Test
    void updateProduct_validProduct() {
        Product product = new Product(1, "Cappucino", 1, "MILK_COFFEE", "DAIRY");

        productService.updateProduct(product);

        verify(validator, times(1)).validate(product);
        verify(productRepo, times(1)).update(product);
    }


    @Test
    void deleteProduct_existingId() {
        int id = 1;

        productService.deleteProduct(id);

        verify(productRepo, times(1)).delete(id);
    }

    @Test
    void findById_existingId() {
        Product product =new Product(1, "Flat White", 1, "MILK_COFFEE", "DAIRY");

        when(productRepo.findOne(1)).thenReturn(product);

        Product result = productService.findById(1);

        assertEquals(product, result);
        verify(productRepo, times(1)).findOne(1);
    }

    @Test
    void getAllProducts() {
        Product p1 =new Product(1, "Flat White", 1, "MILK_COFFEE", "DAIRY");
        Product p2 = new Product(2, "Espresso", 10, "BLACK_COFFEE", "NON-DAIRY");

        when(productRepo.findAll()).thenReturn(List.of(p1, p2));

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));

        verify(productRepo, times(1)).findAll();
    }

    @Test
    void filterByCategorie_one() {
        Product p1 =new Product(1, "Flat White", 1, "MILK_COFFEE", "DAIRY");
        Product p2 =new Product(2, "Cappucino", 5, "MILK_COFFEE", "DAIRY");
        Product p3 = new Product(3, "Espresso", 10, "BLACK_COFFEE", "NON-DAIRY");

        when(productRepo.findAll()).thenReturn(List.of(p1, p2, p3));

        List<Product> result = productService.filterByCategorie("MILK_COFFEE");

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
        assertFalse(result.contains(p3));

        verify(productRepo, times(1)).findAll();
    }

    @Test
    void filterByTip_one() {
        Product p1 =new Product(1, "Flat White", 1, "MILK_COFFEE", "DAIRY");
        Product p2 =new Product(2, "Cappucino", 5, "MILK_COFFEE", "DAIRY");
        Product p3 = new Product(3, "Latte", 10, "BLACK_COFFEE", "NON-DAIRY");

        when(productRepo.findAll()).thenReturn(List.of(p1, p2, p3));

        List<Product> result = productService.filterByTip("DAIRY");

        assertEquals(2, result.size());
        assertTrue(result.contains(p2));
        assertTrue(result.contains(p1));
        assertFalse(result.contains(p3));

        verify(productRepo, times(1)).findAll();
    }

}
