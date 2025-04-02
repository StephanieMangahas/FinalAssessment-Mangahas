package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import pageobjects.*;

import java.time.Duration;

public class OrderCompletionTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPageObject loginPage;
    private ProductPageObject productPage;
    private CheckoutInformationPageObject infoPage;
    private CheckoutOverviewPageObject overviewPage;
    private CheckoutCompletePageObject completePage;

    @BeforeMethod
    public void setUp() {
    	ChromeOptions options = new ChromeOptions();
    	options.addArguments("--headless", "--disable-gpu");
    	
    	driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPageObject(driver);
        productPage = new ProductPageObject(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        loginPage.login("standard_user", "secret_sauce");

        productPage.clickAddToCart(0);
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();

        infoPage = new CheckoutInformationPageObject(driver);
        infoPage.fillForm("John", "Doe", "12345");
        infoPage.clickContinue();

        overviewPage = new CheckoutOverviewPageObject(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFinishButtonNavigatesToCompletePage() {
        overviewPage.clickFinish();
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-complete"));
    }

    @Test
    public void testUserLandsOnCheckoutCompletePage() {
        overviewPage.clickFinish();
        completePage = new CheckoutCompletePageObject(driver);
        Assert.assertTrue(completePage.isThankYouMessageDisplayed());
    }

    @Test
    public void testCheckoutCompletePageDetails() {
        overviewPage.clickFinish();
        completePage = new CheckoutCompletePageObject(driver);
        Assert.assertTrue(completePage.isThankYouMessageDisplayed());
        Assert.assertTrue(completePage.isBackHomeButtonDisplayed());
    }

    @Test
    public void testBackHomeButtonRedirectsToProductPage() {
        overviewPage.clickFinish();
        completePage = new CheckoutCompletePageObject(driver);
        completePage.clickBackHome();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
    }
}
