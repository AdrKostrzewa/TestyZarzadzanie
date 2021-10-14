package database;

import model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopDatabaseTest {

    ShopDatabase testedDb;

    @BeforeEach
    void setUp() {
        testedDb = new ShopDatabase();
        List<Product> produkty = new ArrayList<>();
        produkty.add(new Product(1,"Car","1.png",12.35));
        produkty.add(new Product(2,"Doll","2.png",3.12));
        try {
            Field pField = testedDb.getClass().getDeclaredField("products");
            pField.setAccessible(true);
            pField.set(testedDb, produkty);
            pField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getProductById() {
        Product p = testedDb.getProductById(1);
        assertEquals("Car",p.getName());
        p = testedDb.getProductById(12);
        assertNull(p);
    }

    @Test
    void getProductsByName() {
    }

    @Test
    void getProductsWithPriceInLimits() {
    }
}