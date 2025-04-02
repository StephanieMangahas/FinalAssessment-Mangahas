package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ShoppingCartPageObject {
    private WebDriver driver;
    private WebDriverWait wait;

    private final By cartPageTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By quantityLabel = By.className("cart_quantity");
    private final By descriptionLabel = By.className("inventory_item_desc");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By removeButtons = By.className("cart_button");

    public ShoppingCartPageObject(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void waitForCartToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartPageTitle));
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartItems));
    }

    public boolean isCartPageDisplayed() {
        return driver.findElement(cartPageTitle).isDisplayed();
    }

    public boolean areCartDetailsDisplayed() {
        waitForCartToLoad();
        return driver.findElements(cartItems).size() > 0 &&
               driver.findElement(quantityLabel).isDisplayed() &&
               driver.findElement(descriptionLabel).isDisplayed() &&
               driver.findElement(continueShoppingButton).isDisplayed() &&
               driver.findElement(checkoutButton).isDisplayed();
    }

    public void clickContinueShopping() {
        driver.findElement(continueShoppingButton).click();
    }

    public boolean isProductInCart() {
        waitForCartToLoad();
        return driver.findElements(cartItems).size() > 0;
    }

    public int getCartItemCount() {
        waitForCartToLoad();
        return driver.findElements(cartItems).size();
    }

    public boolean areProductDetailsDisplayed() {
        waitForCartToLoad();
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            if (!item.findElement(By.className("cart_quantity")).isDisplayed() ||
                !item.findElement(By.className("inventory_item_name")).isDisplayed() ||
                !item.findElement(By.className("inventory_item_desc")).isDisplayed() ||
                !item.findElement(By.className("inventory_item_price")).isDisplayed()) {
                return false;
            }
        }
        return true;
    }

    public void removeItemFromCart(int index) {
        waitForCartToLoad();
        List<WebElement> removeBtns = driver.findElements(removeButtons);
        if (removeBtns.size() > index) {
            removeBtns.get(index).click();
        }
    }

    public String getCartCountText() {
        try {
            List<WebElement> badges = driver.findElements(cartBadge);
            if (!badges.isEmpty()) {
                return badges.get(0).getText();
            }
        } catch (Exception ignored) {
        	
        }
        return "0";
    }

    public int getCartBadgeCount() {
        try {
            wait.withTimeout(Duration.ofSeconds(2));
            wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(cartBadge),
                ExpectedConditions.invisibilityOfElementLocated(cartBadge)
            ));

            List<WebElement> badges = driver.findElements(cartBadge);
            if (badges.size() > 0) {
                return Integer.parseInt(badges.get(0).getText());
            }
        } catch (Exception e) {

        }
        return 0;
    }
}
