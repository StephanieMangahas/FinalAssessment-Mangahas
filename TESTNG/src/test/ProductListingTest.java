package test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions; 
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.List;
import pageobjects.LoginPageObject;
import pageobjects.ProductPageObject;

public class ProductListingTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPageObject loginPage;
    private ProductPageObject productPage;

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

        String username = "standard_user";
        String password = "secret_sauce";

        loginPage.login(username, password);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inventory_container")));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testAccessProductListingPage() {
        Assert.assertTrue(productPage.isProductPageDisplayed(), "Product page is not displayed");
    }

    @Test
    public void testAllProductsDisplayed() {
        List<WebElement> products = productPage.getAllProducts();
        Assert.assertFalse(products.isEmpty(), "No products are displayed on the page");
    }

    @Test
    public void testRequiredProductDetailsDisplayed() {
        Assert.assertTrue(productPage.areProductDetailsDisplayed(), "Some product details are missing");
    }

    @Test
    public void testAddToCartButtonFunctionality() {
        productPage.clickAddToCart(0);
        Assert.assertTrue(productPage.isRemoveButtonDisplayed(0), "'Add to Cart' did not change to 'Remove'");
    }

    @Test
    public void testSortByNameAscending() {
        productPage.sortBy("Name (A to Z)");
        Assert.assertTrue(productPage.isSortedByNameAsc(), "Products are not sorted correctly by Name (A to Z)");
    }

    @Test
    public void testSortByNameDescending() {
        productPage.sortBy("Name (Z to A)");
        Assert.assertTrue(productPage.isSortedByNameDesc(), "Products are not sorted correctly by Name (Z to A)");
    }

    @Test
    public void testSortByPriceLowToHigh() {
        productPage.sortBy("Price (low to high)");
        Assert.assertTrue(productPage.isSortedByPriceAsc(), "Products are not sorted correctly by Price (Low to High)");
    }

    @Test
    public void testSortByPriceHighToLow() {
        productPage.sortBy("Price (high to low)");
        Assert.assertTrue(productPage.isSortedByPriceDesc(), "Products are not sorted correctly by Price (High to Low)");
    }
}
