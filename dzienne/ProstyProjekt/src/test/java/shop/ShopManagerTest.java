package shop;

import database.ShopDatabase;
import model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopManagerTest {
    ShopManager testedObj;

    @Mock
    ShopDatabase dbMock;

    @BeforeEach
    void setUp() {
        testedObj  = new ShopManager(dbMock);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addProductToCart() {
        Product p = new Product(1, "Car", "1.png", 13.33);
        when(dbMock.getProductById(1)).thenReturn(p);
        testedObj.addProductToCart(1);
        List<Product> cart = testedObj.showCart();
        assertEquals("Car",cart.get(0).getName());
        assertEquals(1,cart.size());
        Mockito.verify(dbMock, times(1)).getProductById(1);
        Mockito.verify(dbMock, never()).getProductsByName(anyString());
    }
}