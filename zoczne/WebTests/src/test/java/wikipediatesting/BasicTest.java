package wikipediatesting;


import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BasicTest {

    WebDriver wD = null;

    @Before
    public void setup(){
        //Driver https://github.com/mozilla/geckodriver/releases
        System.setProperty(
                "webdriver.gecko.driver",
                "C:\\Programowanie\\Tools\\GekkoDriver\\Firefox\\geckodriver.exe");
        wD = new FirefoxDriver();
        System.setProperty(
                "webdriver.chrome.driver",
                "C:\\Programowanie\\Tools\\GekkoDriver\\Chrome\\chromedriver.exe");
        //wD = new ChromeDriver();

    }

    @Given("Go to Wikipedia Page")
    public void nothingTest(){
        wD.navigate().to("https://pl.wikipedia.org/");
    }

    @When("Do nothing")
    public void doNothing(){

    }

    @Then("Nothing happens")
    public void nothingHappens(){

    }
}
