package test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import pageobjects.LoginPageObject;
import pageobjects.ProductPageObject;
import pageobjects.CartPageObject;

public class CartFunctionalityTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPageObject loginPage;
    private ProductPageObject productPage;
    private CartPageObject cartPage;

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
        cartPage = new CartPageObject(driver);

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
    public void testCartCountUpdatesWhenAddingOneItem() {
        productPage.clickAddToCart(0);
        String cartCount = cartPage.getCartCount();
        Assert.assertEquals(cartCount, "1", "Cart count did not update correctly after adding one item.");
    }

    @Test
    public void testCartCountUpdatesWhenAddingMultipleItems() {
        productPage.clickAddToCart(0);
        productPage.clickAddToCart(1);
        String cartCount = cartPage.getCartCount();
        Assert.assertEquals(cartCount, "2", "Cart count did not update correctly after adding multiple items.");
    }

    @Test
    public void testHamburgerMenuIsDisplayedAndOptionsAreVisible() {
        productPage.openHamburgerMenu();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("bm-menu-wrap")));

        List<String> menuOptions = productPage.getHamburgerMenuOptions();
        List<String> expectedOptions = Arrays.asList("All Items", "About", "Logout", "Reset App State");

        SoftAssert softAssert = new SoftAssert();
        for (String option : expectedOptions) {
            softAssert.assertTrue(menuOptions.contains(option), option + " option is missing.");
        }
        softAssert.assertAll(); // Reports all assertion failures at once
    }

    @Test
    public void testSidebarClosesAfterClickingXButton() {
        productPage.openHamburgerMenu();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("bm-menu-wrap")));

        // Locate and click the close (X) button
        WebElement closeButton = driver.findElement(By.className("bm-cross-button"));

        wait.until(ExpectedConditions.elementToBeClickable(closeButton));

        closeButton.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("bm-menu-wrap")));

        Assert.assertFalse(productPage.isHamburgerMenuOpen(), "Hamburger menu did not close.");
    }
}
