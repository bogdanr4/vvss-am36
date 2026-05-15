package drinkshop.lab4;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductIntegrationTestingR {

    private Repository<Integer, Product> productRepo;
    private Validator<Product> validator;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepo = new FileProductRepository("data/products_test.txt");
        productRepo.findAll().forEach(p -> productRepo.delete(p.getId()));

        validator = new ProductValidator();

        productService = new ProductService(productRepo, validator);

    }

   private void writeProductsToFile(List<Product> products) {

       try(BufferedWriter writer = new BufferedWriter(new FileWriter("data/products_test.txt"))) {
           StringBuilder s = new StringBuilder();

           for(var p : products) {
               s.append(p.getId()).append(",").append(p.getNume()).append(",").append(p.getPret()).
                       append(",").append(p.getCategorie()).append(",").append(p.getTip()).append("\n");

            }
            writer.write(String.valueOf(s));
           writer.flush();
       } catch (IOException e) {
           throw new RuntimeException(e);
       }

   }


    @Test
    void getAllProducts() {
        Product p1 =new Product(1, "Flat White", 1, "MILK_COFFEE", "DAIRY");
        Product p2 = new Product(2, "Espresso", 10, "BLACK_COFFEE", "NON-DAIRY");

        writeProductsToFile(List.of(p1, p2));


        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
    }

    @Test
    void addProduct_validProduct() {




        Product product = new Product(1, "Flat White", 1, "MILK_COFFEE", "DAIRY");

        productService.addProduct(product);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertTrue(result.contains(product));
    }

    @Test
    void addProduct_invalidProduct() {
        Product product =new Product(0, "", 1, "MILK_COFFEE", "DAIRY");;


        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> productService.addProduct(product)
        );

        assertEquals("ID invalid!Invalid Name!", exception.getMessage());

    }

    @Test
    void updateProduct_validProduct() {
        Product product = new Product(1, "Cappucino", 1, "MILK_COFFEE", "DAIRY");

        productService.addProduct(product);

        product.setNume("Flat White");

        productService.updateProduct(product);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertTrue(result.contains(product));
    }


    @Test
    void deleteProduct_existingId() {
        Product product = new Product(1, "Cappucino", 1, "MILK_COFFEE", "DAIRY");

        productService.addProduct(product);

        productService.deleteProduct(product.getId());

        List<Product> result = productService.getAllProducts();

        assertEquals(0,result.size());
    }

    @Test
    void findById_existingId() {
        Product product =new Product(1, "Flat White", 1, "MILK_COFFEE", "DAIRY");
        productService.addProduct(product);

        Product result = productService.findById(1);

        assertEquals(product, result);
    }


    @Test
    void filterByCategorie_one() {
        Product p1 =new Product(1, "Flat White", 1, "MILK_COFFEE", "DAIRY");
        Product p2 =new Product(2, "Cappucino", 5, "MILK_COFFEE", "DAIRY");
        Product p3 = new Product(3, "Espresso", 10, "BLACK_COFFEE", "NON-DAIRY");

        productService.addProduct(p1);
        productService.addProduct(p2);
        productService.addProduct(p3);

        List<Product> result = productService.filterByCategorie("MILK_COFFEE");

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
        assertFalse(result.contains(p3));

    }

    @Test
    void filterByTip_one() {
        Product p1 =new Product(1, "Flat White", 1, "MILK_COFFEE", "DAIRY");
        Product p2 =new Product(2, "Cappucino", 5, "MILK_COFFEE", "DAIRY");
        Product p3 = new Product(3, "Latte", 10, "BLACK_COFFEE", "NON-DAIRY");

        productService.addProduct(p1);
        productService.addProduct(p2);
        productService.addProduct(p3);

        List<Product> result = productService.filterByTip("DAIRY");

        assertEquals(2, result.size());
        assertTrue(result.contains(p2));
        assertTrue(result.contains(p1));
        assertFalse(result.contains(p3));
    }


}
