package evprimefrontendtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pages.BasePage;

import static org.junit.Assert.*;

public class EventDetailsTests extends BasePage {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void updateEventPageUIValidationTest() throws InterruptedException {
        ensureLoggedIn();
        goToEventsPage();
        eventDetailsPage.openActualEvent();
        eventDetailsPage.clickEdit();
        Thread.sleep(1200);
        assertTrue(driver.getPageSource().contains("title"));
    }

    @Test
    public void successfulUpdateEventTest() throws InterruptedException {
        ensureLoggedIn();
        goToEventsPage();
        eventDetailsPage.openActualEvent();
        eventDetailsPage.clickEdit();
        Thread.sleep(1200);
        assertTrue(driver.getPageSource().contains("title"));
    }

    @Test
    public void deleteEventTest() throws InterruptedException {
        ensureLoggedIn();
        goToEventsPage();
        eventDetailsPage.openActualEvent();
        eventDetailsPage.clickDelete();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }
}
