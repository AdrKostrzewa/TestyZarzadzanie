package shop;


import database.ShopDatabase;
import model.Product;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class ShopManagerTest {

    @Mock
    ShopDatabase db;

    ShopManager testObject;

    @BeforeEach
    public void initTestObject(){
        db = mock(ShopDatabase.class);
        testObject = new ShopManager(db);
    }

    @Test
    public void testAdToCart() throws NoSuchFieldException, IllegalAccessException {
        Product p = new Product(1,"Doll","doll.jpg",13.35);
        when(db.getProductById(1)).thenReturn(p);
        testObject.addProductToCart(1);
        Field field = testObject.getClass().getDeclaredField("cart");
        field.setAccessible(true);
        List<Product> cart = (List<Product>) field.get(testObject);
        Product p2 = cart.get(0);
        assertEquals(p,p2);
        assertEquals(1,cart.size());
        field.setAccessible(false);
        verify(db,times(1)).getProductById(1);
        verify(db,never()).getProductsByName(anyString());
    }


    @Test
    public void testAdToCartNotExistingProduct() throws NoSuchFieldException, IllegalAccessException {
        Product p = new Product(1,"Doll","doll.jpg",13.35);
        when(db.getProductById(1)).thenReturn(p);
        boolean result = testObject.addProductToCart(2);
        assertFalse(result);
        Field field = testObject.getClass().getDeclaredField("cart");
        field.setAccessible(true);
        List<Product> cart = (List<Product>) field.get(testObject);
        assertEquals(0,cart.size());
        field.setAccessible(false);
        verify(db,times(1)).getProductById(2);
        verify(db,never()).getProductsByName(anyString());
    }
}
