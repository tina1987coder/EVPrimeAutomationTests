package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public abstract class BasePage {

    public WebDriver driver;
    public Actions actions;
    private WebDriverWait wait;

    protected ChromeOptions options;
    protected SidePanel sidePanel;
    protected CreateUserLoginPage createUserLoginPage;
    protected CreateEditEventPage createEditEventPage;
    protected EventDetailsPage eventDetailsPage;

    protected final String BASE_URL = "http://localhost:3000/";
    protected static final String TEST_EMAIL =
            System.getenv().getOrDefault("EVPRIME_TEST_EMAIL", "arsenal.chelsea.test01@provider.com");
    protected static final String TEST_PASSWORD =
            System.getenv().getOrDefault("EVPRIME_TEST_PASSWORD", "passwordValue");

    public BasePage() {
    }

    public BasePage(WebDriver driver){
        this.driver = driver;
        this.actions = new Actions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void setUp() throws Exception {
        options = new ChromeOptions();
        Path profileDir = Files.createTempDirectory("chrome-profile-");
        options.addArguments("--user-data-dir=" + profileDir.toAbsolutePath());

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));

        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        sidePanel = new SidePanel(driver);
        createUserLoginPage = new CreateUserLoginPage(driver);
        createEditEventPage = new CreateEditEventPage(driver);
        eventDetailsPage = new EventDetailsPage(driver);

        driver.get(BASE_URL);
        Thread.sleep(1200);
    }

    public void goToLoginPage() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(800);
        sidePanel.clickMenuIcon();
        Thread.sleep(250);
        sidePanel.clickLoginButton();
        Thread.sleep(1200);
    }

    public void goToEventsPage() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(800);
        sidePanel.clickMenuIcon();
        Thread.sleep(300);
        sidePanel.clickEventsButton();
        Thread.sleep(1200);
    }

    public void loginUser() throws InterruptedException {
        goToLoginPage();
        createUserLoginPage.insertEmail(TEST_EMAIL);
        createUserLoginPage.insertPassword(TEST_PASSWORD);
        createUserLoginPage.clickGoButton();
        Thread.sleep(1500);
    }

    public void ensureLoggedIn() throws InterruptedException {
        loginUser();
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

    public boolean pageHasText(String text) {
        return driver.getPageSource().toLowerCase().contains(text.toLowerCase());
    }

    public boolean hasValidationError() {
        String p = driver.getPageSource().toLowerCase();
        return p.contains("invalid") || p.contains("error") || p.contains("failed")
                || p.contains("required") || p.contains("must");
    }

    public int countElements(By locator) {
        return driver.findElements(locator).size();
    }

    public int countInputs() {
        return countElements(By.tagName("input"));
    }

    public int countButtons() {
        return countElements(By.tagName("button"));
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}




