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
import utils.ExcelReader;

public class LoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPageObject loginPage;
    private ExcelReader excelReader;
    private static final String FILE_PATH = "src/data/credentials.xlsx"; 
    private static final String SHEET_NAME = "Login";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu"); 

        driver = new ChromeDriver(options); 
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPageObject(driver);
        excelReader = new ExcelReader(FILE_PATH, SHEET_NAME);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginWithValidCredentials() {
        String username = excelReader.getCellData(0, 0).orElse("");
        String password = excelReader.getCellData(0, 1).orElse("");

        loginPage.login(username, password);

        Assert.assertTrue(
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inventory_container"))).isDisplayed(),
            "Login failed!"
        );
    }

    @Test
    public void testLoginWithBlankCredentials() {
        loginPage.login("", "");
        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
    }

    @Test
    public void testLoginWithBlankUsername() {
        String password = excelReader.getCellData(2, 1).orElse("");

        loginPage.login("", password);

        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
    }

    @Test
    public void testLoginWithBlankPassword() {
        String username = excelReader.getCellData(3, 0).orElse("");

        loginPage.login(username, "");

        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Password is required");
    }

    @Test
    public void testLoginWithInvalidUsername() {
        String username = excelReader.getCellData(1, 0).orElse("");
        String password = excelReader.getCellData(1, 1).orElse("");

        loginPage.login(username, password);

        Assert.assertEquals(
            loginPage.getErrorMessage(),
            "Epic sadface: Username and password do not match any user in this service"
        );
    }

    @Test
    public void testLoginWithInvalidPassword() {
        String username = excelReader.getCellData(0, 0).orElse("");
        String password = excelReader.getCellData(1, 1).orElse("");

        loginPage.login(username, password);

        Assert.assertEquals(
            loginPage.getErrorMessage(),
            "Epic sadface: Username and password do not match any user in this service"
        );
    }

    @Test
    public void testLoginPageAccessibility() {
        Assert.assertEquals(loginPage.getPageTitle(), "Swag Labs");
        Assert.assertTrue(loginPage.isLoginPageDisplayed());
    }
}