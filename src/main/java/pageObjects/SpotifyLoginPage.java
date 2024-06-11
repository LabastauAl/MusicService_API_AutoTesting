package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SpotifyLoginPage extends PageObjectBasics {
public SpotifyLoginPage(WebDriver driver){
    super(driver);
}

    @FindBy(xpath = "//input[@id='login-username']")
    private WebElement userNameField;
    @FindBy(xpath = "//input[@id='login-password']")
    private WebElement passwordField;
    @FindBy(xpath = "//button[@id='login-button']")
    private WebElement loginButton;
    @FindBy(xpath = "//input[@data-testid='login-remember']/following-sibling::span")
    private WebElement loginCheckbox;




    public void setUserName(String userName) {
        userNameField.clear();
        userNameField.sendKeys(userName);
    }

    public void setPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void setCheckboxOff() {
        loginCheckbox.click();
    }

    public void loginEnter() {
        loginButton.click();
    }

}
