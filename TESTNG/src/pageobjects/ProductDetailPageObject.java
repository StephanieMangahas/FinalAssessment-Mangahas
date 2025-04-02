package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductDetailPageObject {
    private WebDriver driver;

    private By productImage = By.className("inventory_details_img");
    private By productName = By.className("inventory_details_name");
    private By productDescription = By.className("inventory_details_desc");
    private By productPrice = By.className("inventory_details_price");
    private By addToCartButton = By.cssSelector(".btn_inventory");
    private By removeButton = By.xpath("//button[text()='Remove']");
    private By cartBadge = By.className("shopping_cart_badge");

    public ProductDetailPageObject(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isProductImageDisplayed() {
        return driver.findElement(productImage).isDisplayed();
    }

    public boolean isProductNameDisplayed() {
        return driver.findElement(productName).isDisplayed();
    }

    public boolean isProductDescriptionDisplayed() {
        return driver.findElement(productDescription).isDisplayed();
    }

    public boolean isProductPriceDisplayed() {
        return driver.findElement(productPrice).isDisplayed();
    }

    public boolean isAddToCartButtonDisplayed() {
        return driver.findElement(addToCartButton).isDisplayed();
    }

    public boolean isRemoveButtonDisplayed() {
        return driver.findElements(removeButton).size() > 0;
    }

    public void clickAddToCart() {
        driver.findElement(addToCartButton).click();
    }

    public void clickRemove() {
        driver.findElement(removeButton).click();
    }

    public String getCartCount() {
        return driver.findElements(cartBadge).size() > 0 ? driver.findElement(cartBadge).getText() : "0";
    }
}
