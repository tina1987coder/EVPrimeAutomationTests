package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EventDetailsPage extends BasePage {

    private By editButton = By.xpath("//*[@id=\"root\"]/div/div[1]/main/div[2]/div[1]/div[2]/button[1]");

    private By deleteButton = By.xpath("//*[@id=\"root\"]/div/div[1]/main/div[2]/div[1]/div[1]/div[1]/button");

    private By backButton = By.xpath("//*[@id=\"root\"]/div/div/main/div[2]/div[1]/div[2]/button");

    private By firstEventLink = By.xpath("//*[@id=\"root\"]/div/div/main/div[2]/ul/li[49]/img");

    private By eventTitle = By.xpath("//main//*[contains(normalize-space(.), $title) or contains(@title, $title)]//ancestor-or-self::*[@href or @data-href or @onclick]");

    private By eventCard = By.xpath("//main//div[contains(@class, 'MuiImageListItem-root')]//*[contains(text(), 'Arsenal')]/parent::div");


    public EventDetailsPage(WebDriver driver) {
        super(driver);
    }


    public void clickEdit() throws InterruptedException {
        clickElement(editButton);
    }

    public void clickDelete() throws InterruptedException {
        clickElement(deleteButton);
    }

    public void clickBack() throws InterruptedException {
        clickElement(backButton);
    }

    public void clickFirstEvent() throws InterruptedException {
        clickElement(firstEventLink);
    }

    public void openActualEvent() throws InterruptedException {
        clickFirstEvent();
    }
}
