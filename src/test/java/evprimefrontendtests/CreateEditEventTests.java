package evprimefrontendtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pages.*;


import static org.junit.Assert.*;

public class CreateEditEventTests extends BasePage {


    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void createEventPageUIValidationTest() throws InterruptedException {
        ensureLoggedIn();
        goToEventsPage();
        sidePanel.clickAddEventButton();
        Thread.sleep(1200);

        assertTrue(driver.getPageSource().contains("Title"));
        assertTrue(driver.getPageSource().contains("Image"));
        assertTrue(driver.getPageSource().contains("Date"));
        assertTrue(driver.getPageSource().contains("Location"));
        assertTrue(driver.getPageSource().contains("Description"));
    }

    @Test
    public void successfulCreateEventTest() throws InterruptedException {
        ensureLoggedIn();
        goToEventsPage();
        sidePanel.clickAddEventButton();
        Thread.sleep(1200);

        String title = "Arsenal vs Chelsea " + System.currentTimeMillis();
        createEditEventPage.insertEventTitle(title);
        createEditEventPage.insertEventImage("http://images.com/arsenal-vs-chelsea.jpg");
        createEditEventPage.insertEventDate("2026-03-15");
        createEditEventPage.insertEventLocation("Emirates Stadium, London");
        createEditEventPage.insertEventDescription("Premier League football match");
        createEditEventPage.clickCreateUpdateEventButton();
        Thread.sleep(2000);

        assertTrue(driver.getPageSource().contains(title));
    }

    @Test
    public void emptyTitleCreateEventTest() throws InterruptedException {
        ensureLoggedIn();
        testEmptyField("title", "");
    }

    @Test
    public void emptyDateCreateEventTest() throws InterruptedException {
        ensureLoggedIn();
        testEmptyField("date", "2026-03-15");
    }

    private void testEmptyField(String emptyField, String dateValue) throws InterruptedException {
        goToEventsPage();
        sidePanel.clickAddEventButton();
        Thread.sleep(1200);

        createEditEventPage.insertEventTitle("Test " + System.currentTimeMillis());
        createEditEventPage.insertEventImage("http://images.com/arsenal-vs-chelsea.jpg");
        createEditEventPage.insertEventDate(dateValue);
        createEditEventPage.insertEventLocation("Emirates Stadium");
        createEditEventPage.insertEventDescription("Test");
        createEditEventPage.clickCreateUpdateEventButton();
        Thread.sleep(1200);

        assertTrue(hasValidationError());
    }

    public boolean hasValidationError() {
        String p = driver.getPageSource().toLowerCase();
        return p.contains("invalid");
    }

    @After
    public void tearDown() {
        super.tearDown();
    }
}

