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

public class CheckoutInformationTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPageObject loginPage;
    private ProductPageObject productPage;
    private CheckoutInformationPageObject infoPage;

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
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test public void testAccessCheckoutInformationPage() {
        Assert.assertTrue(infoPage.isInformationPageDisplayed());
    }

    @Test public void testInformationPageRequiredDetailsDisplayed() {
        Assert.assertTrue(infoPage.isInformationPageDisplayed());
        Assert.assertTrue(driver.findElement(By.id("first-name")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("last-name")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("postal-code")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("cancel")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("continue")).isDisplayed());
    }

    @Test public void testCancelButtonRedirectsToCartPage() {
        infoPage.clickCancel();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart.html"));
    }

    @Test public void testBlankFieldsBlockProceed() {
        infoPage.fillForm("", "", "");
        infoPage.clickContinue();
        Assert.assertTrue(infoPage.isErrorMessageDisplayed());
    }

    @Test public void testBlankFirstNameBlocksProceed() {
        infoPage.fillForm("", "Doe", "12345");
        infoPage.clickContinue();
        Assert.assertTrue(infoPage.isErrorMessageDisplayed());
    }

    @Test public void testBlankLastNameBlocksProceed() {
        infoPage.fillForm("John", "", "12345");
        infoPage.clickContinue();
        Assert.assertTrue(infoPage.isErrorMessageDisplayed());
    }

    @Test public void testBlankZipBlocksProceed() {
        infoPage.fillForm("John", "Doe", "");
        infoPage.clickContinue();
        Assert.assertTrue(infoPage.isErrorMessageDisplayed());
    }

    @Test public void testOnlyFirstNameEnteredBlocksProceed() {
        infoPage.fillForm("John", "", "");
        infoPage.clickContinue();
        Assert.assertTrue(infoPage.isErrorMessageDisplayed());
    }

    @Test public void testOnlyLastNameEnteredBlocksProceed() {
        infoPage.fillForm("", "Doe", "");
        infoPage.clickContinue();
        Assert.assertTrue(infoPage.isErrorMessageDisplayed());
    }

    @Test public void testOnlyZipEnteredBlocksProceed() {
        infoPage.fillForm("", "", "12345");
        infoPage.clickContinue();
        Assert.assertTrue(infoPage.isErrorMessageDisplayed());
    }

    @Test public void testValidInfoProceedsToOverview() {
        infoPage.fillForm("John", "Doe", "12345");
        infoPage.clickContinue();
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-two"));
    }
}
