package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class WebDriverSingleton {
    private static WebDriver driver = null;

    private static WebDriver createDriver(String browserType, String driverPath) {
        switch (browserType) {
            case "Chrome":
            case "chrome":
            case "CHROME":
                System.setProperty("webdriver.chrome.driver", driverPath);
                driver = new ChromeDriver();
                break;
            case "FireFox":
            case "Firefox":
            case "FIREFOX":
                System.setProperty("webdriver.firefox.driver", driverPath);
                driver = new FirefoxDriver();
                break;
            default:
                throw new IllegalArgumentException("Value of browser type " + browserType + " is incorrect");
        }
        return driver;
    }

    public static WebDriver setupDriver(String browserType, String driverPath) {
        if (driver == null) {
            driver = createDriver(browserType, driverPath);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            //driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));
            driver.manage().window().maximize();
        }
        return driver;
    }
}
