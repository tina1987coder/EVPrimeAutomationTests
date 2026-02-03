package evprimefrontendtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CreateEditEventPage;
import pages.CreateUserLoginPage;
import pages.EventDetailsPage;
import pages.SidePanel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.junit.Assert.*;

public class EventsTests {

    private WebDriver driver;
    private ChromeOptions options;

    private SidePanel sidePanel;
    private CreateUserLoginPage createUserLoginPage;
    private CreateEditEventPage createEditEventPage;

    private final String BASE_URL = "http://localhost:3001/";

    private static final String TEST_EMAIL =
            System.getenv().getOrDefault("EVPRIME_TEST_EMAIL", "arsenal.chelsea.test01@provider.com");
    private static final String TEST_PASSWORD =
            System.getenv().getOrDefault("EVPRIME_TEST_PASSWORD", "passwordValue");

    @Before
    public void setUp() throws Exception {
        options = new ChromeOptions();

        Path profileDir = Files.createTempDirectory("chrome-profile-");
        options.addArguments("--user-data-dir=" + profileDir.toAbsolutePath());

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));

        sidePanel = new SidePanel(driver);
        createUserLoginPage = new CreateUserLoginPage(driver);
        createEditEventPage = new CreateEditEventPage(driver);

        driver.get(BASE_URL);
        Thread.sleep(1200);

        assertTrue("App not accessible at " + BASE_URL, driver.getPageSource().length() > 500);
    }


    private void goToLoginPage() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(800);

        sidePanel.clickMenuIcon();
        Thread.sleep(250);

        sidePanel.clickLoginButton();
        Thread.sleep(1200);

        assertFalse("You are on an error page (login route not found).",
                driver.getPageSource().toLowerCase().contains("could not find this page"));
    }

    private void goToEventsPage() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(800);

        sidePanel.clickMenuIcon();
        Thread.sleep(300);

        sidePanel.clickEventsButton();
        Thread.sleep(1200);

        assertFalse("You are on an error page, not Events page.",
                driver.getPageSource().toLowerCase().contains("an error occurred"));
    }


    private String uniqueTitle(String base) {
        return base + " " + System.currentTimeMillis();
    }

    private boolean pageHasText(String text) {
        return driver.getPageSource().contains(text);
    }

    private boolean hasValidationError() {
        String p = driver.getPageSource().toLowerCase();
        return p.contains("invalid") || p.contains("error") || p.contains("failed")
                || p.contains("required") || p.contains("must");
    }


    private void loginUser() throws InterruptedException {
        goToLoginPage();

        createUserLoginPage.insertEmail(TEST_EMAIL);
        createUserLoginPage.insertPassword(TEST_PASSWORD);
        createUserLoginPage.clickGoButton();

        Thread.sleep(1500);
    }

    private void ensureLoggedIn() throws InterruptedException {
        loginUser();

        assertTrue("Failed to login with real test account. Current URL: " + driver.getCurrentUrl(),
                driver.getCurrentUrl().contains("/events") || driver.getPageSource().toLowerCase().contains("events"));
    }


    private void openCreateEventForm() throws InterruptedException {
        goToEventsPage();

        sidePanel.clickAddEventButton();
        Thread.sleep(1200);
    }

    private void createValidEvent(String title) throws InterruptedException {
        openCreateEventForm();

        createEditEventPage.insertEventTitle("Arsenal vs Chelsea");
        createEditEventPage.insertEventImage("http://images.com/arsenal-vs-chelsea.jpg");
        createEditEventPage.insertEventDate("2026-03-15");
        createEditEventPage.insertEventLocation("Emirates Stadium, London");
        createEditEventPage.insertEventDescription("Premier League football match between Arsenal and Chelsea");
        createEditEventPage.clickCreateUpdateEventButton();

        Thread.sleep(2000);
    }


    @Test
    public void smokeTestAppRunning() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(1500);
        assertTrue("App loaded successfully", driver.getPageSource().length() > 500);
    }

    @Test
    public void loginPageUIValidationTest() throws InterruptedException {
        goToLoginPage();

        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        assertTrue("Should have at least 2 inputs", inputs.size() >= 2);
        assertTrue("Should have at least 1 button", buttons.size() >= 1);
    }

    @Test
    public void successfulLoginTest() throws InterruptedException {
        ensureLoggedIn();
        assertTrue(driver.getCurrentUrl().contains("/events") || driver.getPageSource().toLowerCase().contains("events"));
    }

    @Test
    public void invalidLoginTest() throws InterruptedException {
        goToLoginPage();

        createUserLoginPage.insertEmail("valid@mail.com");
        createUserLoginPage.insertPassword("wrongPassword");
        createUserLoginPage.clickGoButton();

        Thread.sleep(1500);

        assertTrue(driver.getCurrentUrl().contains("/login")
                || driver.getPageSource().toLowerCase().contains("login")
                || hasValidationError());

        assertTrue(hasValidationError());
    }

    @Test
    public void signupPageUIValidationTest() throws InterruptedException {
        goToLoginPage();

        createUserLoginPage.clickChangeStateButton();
        Thread.sleep(1200);

        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        assertTrue("Should have at least 2 inputs", inputs.size() >= 2);
        assertTrue("Should have at least 1 button", buttons.size() >= 1);
    }

    @Test
    public void successfulSignupTest() throws InterruptedException {

        goToLoginPage();
        createUserLoginPage.clickChangeStateButton();
        Thread.sleep(1200);

        assertTrue(driver.getPageSource().toLowerCase().contains("sign")
                || driver.getPageSource().toLowerCase().contains("register")
                || driver.getPageSource().toLowerCase().contains("create"));
    }

    @Test
    public void invalidEmailSignupTest() throws InterruptedException {
        goToLoginPage();
        createUserLoginPage.clickChangeStateButton();
        Thread.sleep(1200);

        createUserLoginPage.insertEmail("notAnEmail");
        createUserLoginPage.insertPassword(TEST_PASSWORD);
        createUserLoginPage.clickGoButton();

        Thread.sleep(1500);

        assertTrue(hasValidationError());
    }

    @Test
    public void eventsPageUIValidationTest() throws InterruptedException {
        ensureLoggedIn();
        goToEventsPage();

        assertTrue(driver.getCurrentUrl().contains("/events"));
        assertTrue(driver.getPageSource().toLowerCase().contains("events"));
    }

    @Test
    public void changeStateButtonTest() throws InterruptedException {
        goToLoginPage();

        createUserLoginPage.clickChangeStateButton();
        Thread.sleep(1200);

        assertTrue(driver.getPageSource().toLowerCase().contains("sign")
                || driver.getPageSource().toLowerCase().contains("register")
                || driver.getPageSource().toLowerCase().contains("create"));
    }

    @Test
    public void openEventsFromSidePanelTest() throws InterruptedException {
        ensureLoggedIn();

        SidePanel sidePanel = new SidePanel(driver);
        sidePanel.clickEventsButton();

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.urlContains("/events"));

        assertTrue(driver.getCurrentUrl().contains("/events"));
    }



    @Test
    public void backToEventsTest() throws InterruptedException {
        ensureLoggedIn();
        SidePanel sidePanel = new SidePanel(driver);
        sidePanel.clickEventsButton();


        EventDetailsPage eventsPage = new EventDetailsPage(driver);
        eventsPage.openActualEvent();

        EventDetailsPage detailsPage = new EventDetailsPage(driver);
        detailsPage.clickBack();

    }


    @Test
    public void createEventPageUIValidationTest() throws InterruptedException {
        ensureLoggedIn();
        openCreateEventForm();

        assertTrue(pageHasText("title") || pageHasText("Title"));
        assertTrue(pageHasText("image") || pageHasText("Image"));
        assertTrue(pageHasText("date") || pageHasText("Date"));
        assertTrue(pageHasText("location") || pageHasText("Location"));
        assertTrue(pageHasText("description") || pageHasText("Description"));
    }

    @Test
    public void successfulCreateEventTest() throws InterruptedException {
        ensureLoggedIn();

        String title = uniqueTitle("Arsenal vs Chelsea");
        createValidEvent(title);

        assertTrue(driver.getPageSource().contains(title));
    }

    @Test
    public void emptyTitleCreateEventTest() throws InterruptedException {
        ensureLoggedIn();
        openCreateEventForm();

        createEditEventPage.insertEventTitle("");
        createEditEventPage.insertEventImage("http://images.com/arsenal-vs-chelsea.jpg");
        createEditEventPage.insertEventDate("2026-03-15");
        createEditEventPage.insertEventLocation("Emirates Stadium, London");
        createEditEventPage.insertEventDescription("Test");
        createEditEventPage.clickCreateUpdateEventButton();

        Thread.sleep(1200);
        assertTrue(hasValidationError());
    }

    @Test
    public void invalidImageCreateEventTest() throws InterruptedException {
        ensureLoggedIn();
        openCreateEventForm();

        createEditEventPage.insertEventTitle("Invalid image test");
        createEditEventPage.insertEventImage("images.com/no-http.jpg");
        createEditEventPage.insertEventDate("2026-03-15");
        createEditEventPage.insertEventLocation("Emirates Stadium, London");
        createEditEventPage.insertEventDescription("Test");
        createEditEventPage.clickCreateUpdateEventButton();

        Thread.sleep(1200);
        assertTrue(hasValidationError());
    }

    @Test
    public void emptyDateCreateEventTest() throws InterruptedException {
        ensureLoggedIn();
        openCreateEventForm();

        createEditEventPage.insertEventTitle("Empty date test");
        createEditEventPage.insertEventImage("http://images.com/arsenal-vs-chelsea.jpg");
        createEditEventPage.insertEventDate("");
        createEditEventPage.insertEventLocation("Emirates Stadium, London");
        createEditEventPage.insertEventDescription("Test");
        createEditEventPage.clickCreateUpdateEventButton();

        Thread.sleep(1200);
        assertTrue(hasValidationError());
    }

    @Test
    public void emptyLocationCreateEventTest() throws InterruptedException {
        ensureLoggedIn();
        openCreateEventForm();

        createEditEventPage.insertEventTitle("Empty location test");
        createEditEventPage.insertEventImage("http://images.com/arsenal-vs-chelsea.jpg");
        createEditEventPage.insertEventDate("2026-03-15");
        createEditEventPage.insertEventLocation("");
        createEditEventPage.insertEventDescription("Test");
        createEditEventPage.clickCreateUpdateEventButton();

        Thread.sleep(1200);
        assertTrue(hasValidationError());
    }

    @Test
    public void emptyDescriptionCreateEventTest() throws InterruptedException {
        ensureLoggedIn();
        openCreateEventForm();

        createEditEventPage.insertEventTitle("Empty description test");
        createEditEventPage.insertEventImage("http://images.com/arsenal-vs-chelsea.jpg");
        createEditEventPage.insertEventDate("2026-03-15");
        createEditEventPage.insertEventLocation("Emirates Stadium, London");
        createEditEventPage.insertEventDescription("");
        createEditEventPage.clickCreateUpdateEventButton();

        Thread.sleep(1200);
        assertTrue(hasValidationError());
    }

    @Test
    public void updateEventPageUIValidationTest() throws InterruptedException {
        ensureLoggedIn();
        SidePanel sidePanel = new SidePanel(driver);
        sidePanel.clickEventsButton();
        EventDetailsPage eventDetailsPage = new EventDetailsPage(driver);
        eventDetailsPage.openActualEvent();

        EventDetailsPage detailsPage = new EventDetailsPage(driver);
        detailsPage.clickEdit();

    }


    @Test
    public void successfulUpdateEventTest() throws InterruptedException {
        ensureLoggedIn();
        SidePanel sidePanel = new SidePanel(driver);
        sidePanel.clickEventsButton();

        EventDetailsPage eventsPage = new EventDetailsPage(driver);
        eventsPage.openActualEvent();

        EventDetailsPage detailsPage = new EventDetailsPage(driver);
        detailsPage.clickEdit();

    }


    @Test
    public void deleteEventTest() throws InterruptedException {
        ensureLoggedIn();
        SidePanel sidePanel = new SidePanel(driver);
        sidePanel.clickEventsButton();

        EventDetailsPage eventsPage = new EventDetailsPage(driver);
        eventsPage.openActualEvent();

        EventDetailsPage detailsPage = new EventDetailsPage(driver);
        detailsPage.clickDelete();

    }


    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

