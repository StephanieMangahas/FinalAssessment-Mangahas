package test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import pageobjects.LoginPageObject;
import pageobjects.ProductPageObject;
import pageobjects.ProductDetailPageObject;

public class ProductDetailTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPageObject loginPage;
    private ProductPageObject productPage;
    private ProductDetailPageObject productDetailPage;

    @BeforeMethod
    public void setUp() {
    	ChromeOptions options = new ChromeOptions();
    	options.addArguments("--headless", "--disable-gpu");
    	
    	driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        loginPage = new LoginPageObject(driver);
        productPage = new ProductPageObject(driver);
        loginPage.login("standard_user", "secret_sauce");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inventory_container")));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testAccessProductDetailPage() {
        productPage.openProductDetail(0);
        productDetailPage = new ProductDetailPageObject(driver);
        Assert.assertTrue(productDetailPage.isProductNameDisplayed(), "Product detail page did not load properly.");
    }

    @Test
    public void testAllRequiredDetailsDisplayed() {
        productPage.openProductDetail(0);
        productDetailPage = new ProductDetailPageObject(driver);
        
        Assert.assertTrue(productDetailPage.isProductImageDisplayed(), "Product image is missing.");
        Assert.assertTrue(productDetailPage.isProductNameDisplayed(), "Product name is missing.");
        Assert.assertTrue(productDetailPage.isProductDescriptionDisplayed(), "Product description is missing.");
        Assert.assertTrue(productDetailPage.isProductPriceDisplayed(), "Product price is missing.");
        Assert.assertTrue(productDetailPage.isAddToCartButtonDisplayed(), "'Add to Cart' button is missing.");
    }

    @Test
    public void testRemoveButtonDisplayedWhenProductInCart() {
        productPage.openProductDetail(0);
        productDetailPage = new ProductDetailPageObject(driver);
        
        productDetailPage.clickAddToCart();
        Assert.assertTrue(productDetailPage.isRemoveButtonDisplayed(), "'Remove' button is not displayed after adding item.");
    }

    @Test
    public void testRemoveButtonUpdatesCartCount() {
        productPage.openProductDetail(0);
        productDetailPage = new ProductDetailPageObject(driver);

        productDetailPage.clickAddToCart();
        Assert.assertEquals(productDetailPage.getCartCount(), "1", "Cart count did not update correctly.");

        productDetailPage.clickRemove();
        Assert.assertEquals(productDetailPage.getCartCount(), "0", "Cart count did not update correctly after removing item.");
    }

    @Test
    public void testAddToCartButtonUpdatesCartCount() {
        productPage.openProductDetail(0);
        productDetailPage = new ProductDetailPageObject(driver);

        productDetailPage.clickAddToCart();
        Assert.assertEquals(productDetailPage.getCartCount(), "1", "Cart count did not update after clicking 'Add to Cart'.");
    }
}
