package pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ProductPageObject {
    private WebDriver driver;
    private WebDriverWait wait;

    public ProductPageObject(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean isProductPageDisplayed() {
        return isElementDisplayed(By.className("inventory_container"));
    }

    public List<WebElement> getAllProducts() {
        return driver.findElements(By.className("inventory_item"));
    }

    public boolean areProductDetailsDisplayed() {
        return getAllProducts().stream().allMatch(product -> {
            try {
                return product.findElement(By.className("inventory_item_name")).isDisplayed() &&
                        product.findElement(By.className("inventory_item_img")).isDisplayed() &&
                        product.findElement(By.className("inventory_item_desc")).isDisplayed() &&
                        product.findElement(By.className("inventory_item_price")).isDisplayed() &&
                        product.findElement(By.className("btn_inventory")).isDisplayed();
            } catch (NoSuchElementException e) {
                return false;
            }
        });
    }

    public void clickAddToCart(int index) {
        getAllProducts().get(index).findElement(By.className("btn_inventory")).click();
    }

    public boolean isRemoveButtonDisplayed(int index) {
        return getAllProducts().get(index)
                .findElement(By.className("btn_inventory"))
                .getText().equalsIgnoreCase("Remove");
    }

    public void sortBy(String criteria) {
        new Select(driver.findElement(By.className("product_sort_container")))
                .selectByVisibleText(criteria);
    }

    public boolean isSortedByNameAsc() {
        List<String> names = getProductNames();
        return names.equals(names.stream().sorted().collect(Collectors.toList()));
    }

    public boolean isSortedByNameDesc() {
        List<String> names = getProductNames();
        return names.equals(names.stream().sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList()));
    }

    public boolean isSortedByPriceAsc() {
        List<Double> prices = getProductPrices();
        return prices.equals(prices.stream().sorted().collect(Collectors.toList()));
    }

    public boolean isSortedByPriceDesc() {
        List<Double> prices = getProductPrices();
        return prices.equals(prices.stream().sorted((a, b) -> Double.compare(b, a)).collect(Collectors.toList()));
    }

    private List<String> getProductNames() {
        return getAllProducts().stream()
                .map(p -> p.findElement(By.className("inventory_item_name")).getText())
                .collect(Collectors.toList());
    }

    private List<Double> getProductPrices() {
        return getAllProducts().stream()
                .map(p -> Double.parseDouble(p.findElement(By.className("inventory_item_price")).getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    public void openHamburgerMenu() {
        try {
            driver.findElement(By.id("react-burger-menu-btn")).click();
        } catch (NoSuchElementException e) {
            System.out.println("Hamburger menu button not found!");
        }
    }

    public void closeHamburgerMenu() {
        try {
            driver.findElement(By.id("react-burger-cross-btn")).click();
        } catch (NoSuchElementException e) {
            System.out.println("Close button for hamburger menu not found!");
        }
    }

    public boolean isHamburgerMenuDisplayed() {
        return isElementDisplayed(By.id("react-burger-menu-btn"));
    }

    public boolean isHamburgerMenuOpen() {
        return isElementDisplayed(By.className("bm-menu-wrap"));
    }

    public List<WebElement> getHamburgerMenuElements() {
        return driver.findElements(By.cssSelector(".bm-item-list a")); 
    }

    public List<String> getHamburgerMenuOptions() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("bm-menu-wrap"))); 
            List<WebElement> menuItems = getHamburgerMenuElements();
            waitWebElementListAppear(menuItems); 

            List<String> menuOptions = menuItems.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

            System.out.println("Retrieved menu options: " + menuOptions);
            return menuOptions;
        } catch (TimeoutException e) {
            System.out.println("Menu options did not appear in time!");
            return List.of();
        }
    }

    public void waitWebElementListAppear(List<WebElement> elements) {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        } catch (StaleElementReferenceException e) {
            System.out.println("Elements were not stable. Retrying...");
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(elements)));
        }
    }

    private boolean isElementDisplayed(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return driver.findElement(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    
    public void openProductDetail(int index) {
        List<WebElement> products = getAllProducts();
        if (index >= 0 && index < products.size()) {
            products.get(index).findElement(By.className("inventory_item_name")).click();
        } else {
            throw new IndexOutOfBoundsException("Invalid product index: " + index);
        }
    }
    
    
}
