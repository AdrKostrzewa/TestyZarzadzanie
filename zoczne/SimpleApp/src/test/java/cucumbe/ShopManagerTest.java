package cucumbe;

import database.ShopDatabase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.Product;
import org.mockito.Mock;
import shop.ShopManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class ShopManagerTest {

    ShopManager testObject;

    @Mock
    ShopDatabase db;
    Product lastAdded = null;
    int lastId=0;


    @Given("I have an empty Cart")
    public void createEmptyCart() {
        db = mock(ShopDatabase.class);
        testObject = new ShopManager(db);

    }

    @Given("I have a non empty Cart")
    public void createNonEmptyCart() {
        createEmptyCart();
        addSingleProductToCart();
    }

    @When("I add a new Product into a Cart")
    public void addSingleProductToCart() {
        Product p = new Product(lastId++, "Doll", "d.jpg", 33.2);
        when(db.getProductById(lastId)).thenReturn(p);
        testObject.addProductToCart(lastId);
        lastAdded = p;
    }

    @When("I erase a last Product form a Cart")
    public void eraseLastProductFromCart(){
    }

    @Then("I obtain Cart with lenght {int}")
    public void checkLenghtOfCart(int expectedSize) {
        int size = testObject.showCart().size();
        assertEquals(expectedSize, size);
    }

    @Then("Database was asked {int} times about product with id {int}")
    public void checkQueriesToDb(int noOfTimes, int onProduct){
        verify(db, times(noOfTimes)).getProductById(onProduct);
        verify(db, never()).getProductsByName(anyString());
    }

    @Then("I obtain an empty Cart")
    public void checkIfCartIsEmpty(){
        checkLenghtOfCart(0);
    }

    @Then("I obtain the same Product on last place in Cart")
    public void checkTheLastProduct(){
        int size = testObject.showCart().size();
        Product p = testObject.showCart().get(size-1);
        assertEquals(lastAdded,p);
    }
}
