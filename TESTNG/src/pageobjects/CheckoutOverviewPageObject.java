package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutOverviewPageObject {
    private WebDriver driver;

    private By header = By.className("title");
    private By finishBtn = By.id("finish");
    private By cancelBtn = By.id("cancel");

    public CheckoutOverviewPageObject(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isOverviewPageDisplayed() {
        return driver.findElement(header).getText().contains("Overview") ||
               driver.findElement(header).getText().contains("Checkout: Overview");
    }

    public void clickFinish() {
        driver.findElement(finishBtn).click();
    }

    public void clickCancel() {
        driver.findElement(cancelBtn).click();
    }
}