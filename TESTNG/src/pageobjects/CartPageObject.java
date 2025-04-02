package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

public class CartPageObject {
    private WebDriver driver;

    public CartPageObject(WebDriver driver) {
        this.driver = driver;
    }

    public String getCartCount() {
        try {
            return driver.findElement(By.className("shopping_cart_badge")).getText();
        } catch (NoSuchElementException e) {
            return "0"; 
        }
    }

    public void openCart() {
        driver.findElement(By.className("shopping_cart_link")).click();
    }

    public List<String> getCartItems() {
        return driver.findElements(By.className("cart_item")).stream()
                .map(item -> item.findElement(By.className("inventory_item_name")).getText())
                .collect(Collectors.toList());
    }

    public void removeItem(int index) {
        List<WebElement> removeButtons = driver.findElements(By.className("cart_button"));
        if (index >= 0 && index < removeButtons.size()) {
            removeButtons.get(index).click();
        }
    }

    public void emptyCart() {
        driver.findElements(By.className("cart_button"))
                .forEach(WebElement::click);
    }

    public boolean isCartEmpty() {
        return driver.findElements(By.className("cart_item")).isEmpty();
    }
}
