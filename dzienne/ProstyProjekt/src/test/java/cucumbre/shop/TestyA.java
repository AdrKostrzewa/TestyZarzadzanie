package cucumbre.shop;

import database.ShopDatabase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import model.Product;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import shop.ShopManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestyA {
    ShopManager testedObj;

    @Mock
    ShopDatabase dbMock;

    Product lastAddedProduct;
    int lastId =0;

    @Given("We have an empty Cart")
    public void createEmptyCart(){
        dbMock = mock(ShopDatabase.class);
        testedObj  = new ShopManager(dbMock);
    }

    @Given("We have a non empty Cart")
    public void createNotEmptyCart(){
        dbMock = mock(ShopDatabase.class);
        testedObj  = new ShopManager(dbMock);
        int id= lastId++;
        Product p = new Product(id, "Doll", id+".png", 23.33);
        when(dbMock.getProductById(id)).thenReturn(p);
        testedObj.addProductToCart(id);
        lastAddedProduct = p;
    }

    @When("We add a new Product into a Cart")
    public void addElementToACar(){
        int id= lastId++;
        Product p = new Product(id, "Car", id+".png", 13.33);
        when(dbMock.getProductById(id)).thenReturn(p);
        testedObj.addProductToCart(id);
        lastAddedProduct = p ;
    }

    @When("We erase a last Product form a Cart")
    public void eraseLastElementFromACart(){
        int size  = testedObj.showCart().size();
        Product last = testedObj.showCart().get(size-1);
        boolean success = testedObj.removeFromCart(last);
        assertTrue(success);
    }

    @When("We add a new Product {string} into a Cart")
    public void addProductToACart(String productName){
        int id= lastId++;
        Product p = new Product(id, productName, id+".png", 82.33);
        List<Product> list = new ArrayList<>();
        list.add(p);
        when(dbMock.getProductsByName(productName)).thenReturn(list);
        testedObj.addProductToCart(productName);
        lastAddedProduct = p;
    }

    @When("We erase a {string} Product form a Cart")
    public void eraseProductByName(String productName){
        testedObj.removeFromCart(productName);
    }

    @Then("We obtain Cart with lenght {int}")
    public void checkSizeOfTheCart(int expectedSize){
        int actualSize = testedObj.showCart().size();
        assertEquals(expectedSize,actualSize);
    }

    @Then("We obtain the same Product on last place in Cart")
    public void checkIfLastElementOfCartIsTheSame(){
        int size  = testedObj.showCart().size();
        Product last = testedObj.showCart().get(size-1);
        assertEquals(lastAddedProduct,last);
    }

    @Then("We obtain an empty Cart")
    public void CheckIfCartIsEmty(){
        checkSizeOfTheCart(0);
    }

}
