package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePageObject {
    private WebDriver driver;

    private By header = By.className("complete-header"); 
    private By backHomeBtn = By.id("back-to-products");

    public CheckoutCompletePageObject(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isThankYouMessageDisplayed() {
        return driver.findElement(header).isDisplayed();
    }

    public boolean isBackHomeButtonDisplayed() {
        return driver.findElement(backHomeBtn).isDisplayed();
    }

    public void clickBackHome() {
        driver.findElement(backHomeBtn).click();
    }
}
