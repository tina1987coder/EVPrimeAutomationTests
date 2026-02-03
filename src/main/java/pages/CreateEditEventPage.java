package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CreateEditEventPage extends BasePage {

    private By eventTitle = By.name("title");
    private By eventImage = By.name("image");
    private By eventDate = By.name("date");
    private By eventLocation = By.name("location");
    private By eventDescription = By.cssSelector("textarea[name='description']");
    private By createUpdateEventButton = By.xpath("//*[@id=\"root\"]/div/div[1]/main/div[2]/form/div/button");

    public CreateEditEventPage(WebDriver driver) {
        super(driver);
    }


    public void insertEventTitle(String value) {
        insertText(eventTitle, value);
    }

    public void insertEventImage(String value) {
        insertText(eventImage, value);
    }

    public void insertEventDate(String value) {
        insertText(eventDate, value);
    }

    public void insertEventLocation(String value) {
        insertText(eventLocation, value);
    }

    public void insertEventDescription(String value) {
        insertText(eventDescription, value);
    }

    public void clickCreateUpdateEventButton() throws InterruptedException {
        clickElement(createUpdateEventButton);
    }
}



