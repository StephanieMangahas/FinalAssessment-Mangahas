package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInformationPageObject {
    private WebDriver driver;

    private By header = By.className("title");
    private By firstName = By.id("first-name");
    private By lastName = By.id("last-name");
    private By postalCode = By.id("postal-code");
    private By cancelBtn = By.id("cancel");
    private By continueBtn = By.id("continue");
    private By errorMessage = By.className("error-message-container");

    public CheckoutInformationPageObject(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isInformationPageDisplayed() {
        return driver.findElement(header).getText().contains("Your Information");
    }

    public void fillForm(String fName, String lName, String zip) {
        driver.findElement(firstName).clear();
        driver.findElement(firstName).sendKeys(fName);
        driver.findElement(lastName).clear();
        driver.findElement(lastName).sendKeys(lName);
        driver.findElement(postalCode).clear();
        driver.findElement(postalCode).sendKeys(zip);
    }

    public void clickContinue() {
        driver.findElement(continueBtn).click();
    }

    public void clickCancel() {
        driver.findElement(cancelBtn).click();
    }

    public boolean isErrorMessageDisplayed() {
        return driver.findElements(errorMessage).size() > 0 && driver.findElement(errorMessage).isDisplayed();
    }
}