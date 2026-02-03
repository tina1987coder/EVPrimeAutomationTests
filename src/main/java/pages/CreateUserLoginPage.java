package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CreateUserLoginPage extends BasePage {

    private By formTitle = By.cssSelector("form");

    private By emailTextBox = By.cssSelector(
            "input[type='email'], input[name='email'], input[id*='email' i], input[placeholder*='email' i]"
    );

    private By passwordTextBox = By.cssSelector(
            "input[type='password'], input[name='password'], input[id*='password' i], input[placeholder*='password' i]"
    );

    private By goButton = By.xpath(
            "//button[contains(.,'Go') or contains(.,'Login') or contains(.,'Sign') or contains(.,'Create') or contains(.,'Register')]"
    );

    private By changeStateButton = By.xpath(
            "//button[contains(.,'Sign up') or contains(.,'Create') or contains(.,'Register') or contains(.,'Login')]"
    );

    public CreateUserLoginPage(WebDriver driver) {
        super(driver);
    }

    public String getTitleText() {
        return getTextFromElement(formTitle);
    }

    public void insertEmail(String value) {
        insertText(emailTextBox, value);
    }

    public void insertPassword(String value) {
        insertText(passwordTextBox, value);
    }

    public void clickGoButton() throws InterruptedException {
        clickElement(goButton);
    }

    public void clickChangeStateButton() throws InterruptedException {
        clickElement(changeStateButton);
    }
}

