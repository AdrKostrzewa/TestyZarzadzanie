package cucumbre.shop;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProduct {
    Product pA, pB;
    boolean result;

    int lastid=0;

    @Given("We have a product {string} with price {float}")
    public void createProduct(String name, float price){
        if (pA == null) {
            pA = new Product(lastid++, name, name + "jpg", price);
        }
        pB = new Product(lastid++, name, name + "jpg", price);
    }

    @When("We compare both products")
    public void compareBoth(){
        result = pA.equals(pB);
    }

    @Then("We obtain true")
    public void checkCompare(){
        assertEquals(true, result);
    }
}
