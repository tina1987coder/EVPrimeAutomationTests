package evprimefrontendtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.BasePage;

import java.util.List;

import static org.junit.Assert.*;

public class CreateUserLoginTests extends BasePage {


    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void smokeTestAppRunning() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(1500);
        assertTrue(driver.getPageSource().length() > 500);
    }

    @Test
    public void loginPageUIValidationTest() throws InterruptedException {
        goToLoginPage();
        assertTrue(countInputs() >= 2);
        assertTrue(countButtons() >= 1);
    }

    @Test
    public void successfulLoginTest() throws InterruptedException {
        ensureLoggedIn();
        assertTrue(driver.getCurrentUrl().contains("http://localhost:3000/"));
    }

    @Test
    public void invalidLoginTest() throws InterruptedException {
        goToLoginPage();
        createUserLoginPage.insertEmail("valid@mail.com");
        createUserLoginPage.insertPassword("wrongPassword");
        createUserLoginPage.clickGoButton();
        Thread.sleep(1500);
        assertTrue(hasValidationError());
    }

    @Test
    public void signupPageUIValidationTest() throws InterruptedException {
        goToLoginPage();
        createUserLoginPage.clickChangeStateButton();
        Thread.sleep(1200);
        assertTrue(countInputs() >= 2);
        assertTrue(countButtons() >= 1);
    }


    @After
    public void tearDown() {
        super.tearDown();
    }
}

