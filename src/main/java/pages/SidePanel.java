package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SidePanel extends BasePage {

    private By menuIcon = By.cssSelector("header button[aria-label*='drawer' i]");
    private By eventsButton = By.xpath("//a[normalize-space(.)='Events'] | //button[normalize-space(.)='Events']");
    private By loginButton = By.xpath("//span[text()='Login']");
    private By plusButton = By.cssSelector("button[aria-label='SpeedDial'], button.MuiFab-root, .MuiFab-root");
    private By addEventButton = By.xpath("//button[.//*[@data-testid='EventIcon']]");

    public SidePanel(WebDriver driver) {
        super(driver);
    }

    public void clickMenuIcon() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(menuIcon)).click();
    }

    public void clickEventsButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        By anyEventsText = By.xpath("//*[normalize-space(.)='Events' or normalize-space(text())='Events']");

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(anyEventsText));
        List<WebElement> candidates = driver.findElements(anyEventsText);

        System.out.println("Events text candidates found: " + candidates.size());

        WebElement best = null;

        for (WebElement el : candidates) {
            if (!el.isDisplayed()) continue;

            Boolean insideSidebar = (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "const el = arguments[0];" +
                            "return !!el.closest('nav,aside,[role=\"navigation\"],[role=\"menu\"],.MuiDrawer-root,.MuiDrawer-paper');",
                    el
            );
            if (insideSidebar != null && insideSidebar) {
                best = el;
                break;
            }
        }

        if (best == null) {
            int shown = 0;
            for (WebElement el : candidates) {
                if (!el.isDisplayed()) continue;
                String html = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].outerHTML;", el);
                System.out.println("VISIBLE Events candidate HTML: " + html);
                shown++;
                if (shown >= 3) break;
            }
            throw new NoSuchElementException("No visible 'Events' found inside sidebar/nav/drawer containers.");
        }

        WebElement clickable = (WebElement) ((JavascriptExecutor) driver).executeScript(
                "const el = arguments[0];" +
                        "return el.closest('a,button,[role=\"button\"],li,div');", best);

        if (clickable == null) {
            clickable = best;
        }

        wait.until(ExpectedConditions.elementToBeClickable(clickable));

        try {
            clickable.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickable);
        }
    }

    public void clickLoginButton() throws InterruptedException {
        clickElement(loginButton);
    }


    public void clickAddEventButton() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        waitUntilLoggedIn(wait);

        wait.until(ExpectedConditions.elementToBeClickable(plusButton));
        driver.findElement(plusButton).click();

        wait.until(ExpectedConditions.elementToBeClickable(addEventButton));
        clickElement(addEventButton);
    }

    private void waitUntilLoggedIn(WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loginButton));
        } catch (Exception e) {
            throw new RuntimeException("User is not logged in (Login button still visible), so '+' is not available.", e);
        }
    }
}

