package pageObjects;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PageObjectBasics {
    private WebDriver driver;

    public PageObjectBasics(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WebElement waitForVisibility(WebElement element) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException exc) {
            System.out.println("Element \"" + element + "\" is not visible on the page now");
            //System.out.println(exc);
        }
        return element;
    }

    public WebDriver getDriverInstance() {
        return driver;
    }

}
