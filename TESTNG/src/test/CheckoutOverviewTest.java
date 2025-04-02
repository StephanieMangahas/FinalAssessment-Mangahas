package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import pageobjects.*;

import java.time.Duration;

public class CheckoutOverviewTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPageObject loginPage;
    private ProductPageObject productPage;
    private CheckoutInformationPageObject infoPage;
    private CheckoutOverviewPageObject overviewPage;

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

    @Test public void testAccessCheckoutOverviewPage() {
        Assert.assertTrue(overviewPage.isOverviewPageDisplayed());
    }

    @Test public void testOverviewPageRequiredDetailsDisplayed() {
        Assert.assertTrue(driver.findElement(By.className("cart_item")).isDisplayed());
        Assert.assertTrue(driver.getPageSource().contains("Payment Information"));
        Assert.assertTrue(driver.getPageSource().contains("Shipping Information"));
        Assert.assertTrue(driver.getPageSource().contains("Item total"));
        Assert.assertTrue(driver.findElement(By.id("cancel")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("finish")).isDisplayed());
    }

    @Test public void testCancelButtonReturnsToProductPage() {
        overviewPage.clickCancel();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
    }

    @Test
    public void testCartItemDetailsDisplayed() {
        WebElement item = driver.findElement(By.className("cart_item"));
        Assert.assertTrue(item.findElement(By.className("cart_quantity")).isDisplayed());
        Assert.assertTrue(item.findElement(By.className("inventory_item_name")).isDisplayed());
        Assert.assertTrue(item.findElement(By.className("inventory_item_desc")).isDisplayed());
        Assert.assertTrue(item.findElement(By.className("inventory_item_price")).isDisplayed());
    }

    @Test public void testTotalPriceCalculationIsCorrect() {
        String itemTotalText = driver.findElement(By.className("summary_subtotal_label")).getText(); 
        String taxText = driver.findElement(By.className("summary_tax_label")).getText(); 
        String totalText = driver.findElement(By.className("summary_total_label")).getText(); 

        double itemTotal = Double.parseDouble(itemTotalText.replace("Item total: $", ""));
        double tax = Double.parseDouble(taxText.replace("Tax: $", ""));
        double total = Double.parseDouble(totalText.replace("Total: $", ""));

        Assert.assertEquals(itemTotal + tax, total, 0.01, "Total price is not calculated correctly.");
    }
}
