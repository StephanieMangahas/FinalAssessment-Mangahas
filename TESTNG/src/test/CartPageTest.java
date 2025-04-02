package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pageobjects.ShoppingCartPageObject;
import pageobjects.LoginPageObject;
import pageobjects.ProductPageObject;

import java.time.Duration;

public class CartPageTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPageObject loginPage;
    private ProductPageObject productPage;
    private ShoppingCartPageObject cartPage;

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
    public void testAccessCartPage() {
        driver.findElement(By.className("shopping_cart_link")).click();
        cartPage = new ShoppingCartPageObject(driver);
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page did not load.");
    }

    @Test
    public void testCartDetailsDisplayed() {
        productPage.clickAddToCart(0);
        driver.findElement(By.className("shopping_cart_link")).click();
        cartPage = new ShoppingCartPageObject(driver);
        cartPage.waitForCartToLoad();
        Assert.assertTrue(cartPage.areCartDetailsDisplayed(), "Not all cart details are displayed.");
    }

    @Test
    public void testContinueShoppingNavigatesToProductPage() {
        driver.findElement(By.className("shopping_cart_link")).click();
        cartPage = new ShoppingCartPageObject(driver);
        cartPage.clickContinueShopping();
        Assert.assertTrue(productPage.isProductPageDisplayed(), "Did not navigate back to product page.");
    }

    @Test
    public void testProductDisplayedInCart() {
        productPage.clickAddToCart(0);
        driver.findElement(By.className("shopping_cart_link")).click();
        cartPage = new ShoppingCartPageObject(driver);
        Assert.assertTrue(cartPage.isProductInCart(), "Added product is not displayed in the cart.");
    }

    @Test
    public void testMultipleProductsDisplayedInCart() {
        productPage.clickAddToCart(0);
        productPage.clickAddToCart(1);
        driver.findElement(By.className("shopping_cart_link")).click();
        cartPage = new ShoppingCartPageObject(driver);
        Assert.assertTrue(cartPage.getCartItemCount() >= 2, "Not all added products are displayed in the cart.");
    }

    @Test
    public void testProductDetailsDisplayedInCart() {
        productPage.clickAddToCart(0);
        driver.findElement(By.className("shopping_cart_link")).click();
        cartPage = new ShoppingCartPageObject(driver);
        Assert.assertTrue(cartPage.areProductDetailsDisplayed(), "Not all product details are displayed.");
    }

    @Test
    public void testRemoveItemUpdatesCartCount() {
        productPage.clickAddToCart(0);
        driver.findElement(By.className("shopping_cart_link")).click();
        cartPage = new ShoppingCartPageObject(driver);

        Assert.assertEquals(cartPage.getCartBadgeCount(), 1, "Cart count incorrect after adding item.");

        cartPage.removeItemFromCart(0);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("shopping_cart_badge")));

        Assert.assertEquals(cartPage.getCartBadgeCount(), 0, "Cart count incorrect after removing item.");
    }

    @Test
    public void testRemoveAllItemsUpdatesCartCount() {
        productPage.clickAddToCart(0);
        productPage.clickAddToCart(1);
        driver.findElement(By.className("shopping_cart_link")).click();
        cartPage = new ShoppingCartPageObject(driver);

        Assert.assertEquals(cartPage.getCartBadgeCount(), 2, "Cart count incorrect after adding items.");

        cartPage.removeItemFromCart(1); 
        cartPage.removeItemFromCart(0);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("shopping_cart_badge")));

        Assert.assertEquals(cartPage.getCartBadgeCount(), 0, "Cart count incorrect after removing all items.");
    }

    @Test
    public void testCartPersistsAfterRelogin() {
        productPage.clickAddToCart(0);
        driver.findElement(By.className("shopping_cart_link")).click();
        cartPage = new ShoppingCartPageObject(driver);
        cartPage.waitForCartToLoad();

        Assert.assertTrue(cartPage.isProductInCart(), "Product not in cart before logout.");

        loginPage.logout();
        loginPage.login("standard_user", "secret_sauce");

        driver.findElement(By.className("shopping_cart_link")).click();
        cartPage = new ShoppingCartPageObject(driver);
        cartPage.waitForCartToLoad();

        Assert.assertTrue(cartPage.isProductInCart(), "Cart items did not persist after relogin.");
    }
}
