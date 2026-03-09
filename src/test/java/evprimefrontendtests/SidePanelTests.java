package evprimefrontendtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pages.BasePage;

import static org.junit.Assert.*;

public class SidePanelTests extends BasePage {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void openEventsFromSidePanelTest() throws InterruptedException {
        ensureLoggedIn();
        sidePanel.clickEventsButton();
        Thread.sleep(1200);
        assertTrue(createUserLoginPage.pageHasText("events"));
    }

    @Test
    public void addEventButtonTest() throws InterruptedException {
        ensureLoggedIn();
        goToEventsPage();
        sidePanel.clickAddEventButton();
        Thread.sleep(1200);
        assertTrue(createEditEventPage.pageHasText("title"));
    }

    @Test
    public void menuIconTest() throws InterruptedException {
        driver.get(BASE_URL);
        sidePanel.clickMenuIcon();
        Thread.sleep(800);
        assertTrue(sidePanel.pageHasText("Events") || sidePanel.pageHasText("Login"));
    }

    @Test
    public void loginButtonTest() throws InterruptedException {
        driver.get(BASE_URL);
        sidePanel.clickMenuIcon();
        Thread.sleep(250);
        sidePanel.clickLoginButton();
        Thread.sleep(1200);
        assertTrue(createUserLoginPage.pageHasText("login"));
    }

    @After
    public void tearDown() {
        super.tearDown();
    }
}
