package drinkshop.lab3;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.service.validator.RetetaValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RetetaValidatorTest {
    private RetetaValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RetetaValidator();
    }

    private IngredientReteta ing(String denumire, double cantitate) {
        return new IngredientReteta(denumire, cantitate);
    }

    @Test
    void F02_TC01_validData() {
        Reteta r = new Reteta(1, List.of(
                ing("zahar", 1)
        ));

        assertDoesNotThrow(() -> validator.validate(r));
    }

    @Test
    void F02_TC02_invalidId() {
        Reteta r = new Reteta(0, List.of(
                ing("zahar", 1)
        ));

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(r)
        );

        assertTrue(ex.getMessage().contains("Product ID invalid"));
    }

    @Test
    void F02_TC03_emptyList() {
        Reteta r = new Reteta(1, new ArrayList<>());

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(r)
        );

        assertTrue(ex.getMessage().contains("Ingrediente empty"));
    }

    @Test
    void F02_TC04_quantityZero() {
        Reteta r = new Reteta(1, List.of(
                ing("faina", 0)
        ));

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(r)
        );

        assertTrue(ex.getMessage().contains("[faina]cantitate negativa sau zero"));

    }

    @Test
    void F02_TC05_multipleErrors() {
        Reteta r = new Reteta(0, new ArrayList<>());

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(r)
        );

        assertTrue(ex.getMessage().contains("Product ID invalid"));
        assertTrue(ex.getMessage().contains("Ingrediente empty"));
    }

    @Test
    void F02_TC06_loop1Iteration_noException() {
        Reteta r = new Reteta(1, List.of(
                ing("lapte", 1)
        ));

        assertDoesNotThrow(() -> validator.validate(r));
    }

    @Test
    void F02_TC07_loopMultiple() {
        List<IngredientReteta> lista = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            lista.add(ing("ing" + i, 1));
        }

        Reteta r = new Reteta(1, lista);

        assertDoesNotThrow(() -> validator.validate(r));
    }

    @Test
    void F02_TC08_loopNminus1() {
        int n = 5;
        List<IngredientReteta> lista = new ArrayList<>();

        for (int i = 0; i < n - 1; i++) {
            lista.add(ing("ing" + i, 1));
        }

        Reteta r = new Reteta(1, lista);

        assertDoesNotThrow(() -> validator.validate(r));
    }

    @Test
    void F02_TC09_loopN() {
        int n = 5;
        List<IngredientReteta> lista = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            lista.add(ing("ing" + i, 1));
        }

        Reteta r = new Reteta(1, lista);

        assertDoesNotThrow(() -> validator.validate(r));
    }

    @Test
    void F02_TC10_loopNplus1() {
        int n = 5;
        List<IngredientReteta> lista = new ArrayList<>();

        for (int i = 0; i < n + 1; i++) {
            lista.add(ing("ing" + i, 1));
        }

        Reteta r = new Reteta(1, lista);

        assertDoesNotThrow(() -> validator.validate(r));
    }

    @Test
    void F02_TC11() {
        Reteta r = new Reteta(1, null);

        assertThrows(NullPointerException.class, () -> validator.validate(r));
    }
}
