package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    public WebDriver driver;
    public Actions actions;

    private WebDriverWait wait;

    public BasePage(WebDriver driver){
        this.driver = driver;
        this.actions = new Actions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }


    private WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private void scrollTo(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception ignored) {}
    }

    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public boolean isPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }


    public void clickElement(By locator) {
        WebElement el = waitClickable(locator);
        scrollTo(el);

        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            jsClick(el);
        } catch (StaleElementReferenceException e) {
            el = waitClickable(locator);
            scrollTo(el);
            jsClick(el);
        } catch (Exception e) {
            jsClick(el);
        }
    }


    public void insertText(By locator, String text){
        WebElement el = waitVisible(locator);
        scrollTo(el);

        el.click();
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.DELETE);
        el.sendKeys(text);
    }

    public void hoverElement(By locator){
        WebElement el = waitVisible(locator);
        scrollTo(el);

        actions.moveToElement(el).perform();
    }

    public String getTextFromElement(By locator){
        return waitVisible(locator).getText();
    }
}


