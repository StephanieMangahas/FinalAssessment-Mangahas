package pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPageObject {
    private WebDriver driver;
    private WebDriverWait wait;

    public LoginPageObject(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.cssSelector("h3[data-test='error']");
    private By inventoryPage = By.id("inventory_container");

    private By menuButton = By.id("react-burger-menu-btn");
    private By logoutButton = By.id("logout_sidebar_link");

    public void enterUsername(String username) {
        WebElement userField = driver.findElement(usernameField);
        userField.clear();
        userField.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passField = driver.findElement(passwordField);
        passField.clear();
        passField.sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public boolean isLoginSuccessful() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryPage)).isDisplayed();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isLoginPageDisplayed() {
        return driver.findElement(usernameField).isDisplayed() &&
               driver.findElement(passwordField).isDisplayed() &&
               driver.findElement(loginButton).isDisplayed();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public void logout() {
        driver.findElement(menuButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton)).click(); 
    }
}
