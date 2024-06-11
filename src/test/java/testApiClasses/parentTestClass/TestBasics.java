package testApiClasses.parentTestClass;

import requestPayloadJson.JsonConverter;
import io.restassured.RestAssured;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import pageObjects.SpotifyLoginPage;
import utils.Base64coder;
import utils.PropertiesDataReader;
import utils.WebDriverSingleton;

import static io.restassured.RestAssured.given;

public class TestBasics {
    protected static WebDriver driver = null;

    protected static String spotifyAuthorizationUrl;
    protected static String accessTokenUrl;
    protected static String scope;
    protected static String appRedirectUri;
    protected static String appClientId;
    protected static String appClientSecret;

    protected static String accountUserName;
    protected static String accountPassword;

    protected TestBasics() {
        if (spotifyAuthorizationUrl == null) {
            spotifyAuthorizationUrl = PropertiesDataReader.getPropertyValue("spotify_authorization_url");
        }
        if (accessTokenUrl == null) {
            accessTokenUrl = PropertiesDataReader.getPropertyValue("access_token_url");
        }
        if (appRedirectUri == null) {
            appRedirectUri = PropertiesDataReader.getPropertyValue("app_redirect_uri");
        }
        if (appClientId == null) {
            appClientId = PropertiesDataReader.getPropertyValue("app_client_id");
        }
        if (appClientSecret == null) {
            appClientSecret = PropertiesDataReader.getPropertyValue("app_client_secret");
        }
        if (scope == null) {
            scope = PropertiesDataReader.getPropertyValue("scope");
        }
        if (accountUserName == null) {
            accountUserName = PropertiesDataReader.getPropertyValue("user_name");
        }
        if (accountPassword == null) {
            accountPassword = PropertiesDataReader.getPropertyValue("password");
        }

        driver = WebDriverSingleton.setupDriver(PropertiesDataReader.getPropertyValue("browser_type"),
                PropertiesDataReader.getPropertyValue("driver_path"));
    }

    protected static String authorizationPageUri;
    protected SpotifyLoginPage spotifyLoginPage;

    @BeforeSuite(enabled = true)
    protected void startActions() throws InterruptedException {
        System.out.println("Webdriver instance " + "\"" + getClass() + "\" class. " + driver);
        spotifyLoginPage = new SpotifyLoginPage(driver);

        authorizationPageUri = spotifyAuthorizationUrl + "?" + "client_id=" + appClientId + "&response_type=code" + "&scope=" + scope + "&redirect_uri=" + appRedirectUri;
        System.out.println("Full authorization page URI:\n" + authorizationPageUri);

        driver.get(authorizationPageUri);
        spotifyLoginPage.setUserName(accountUserName);
        spotifyLoginPage.setPassword(accountPassword);
        spotifyLoginPage.setCheckboxOff();
        spotifyLoginPage.loginEnter();

        Thread.sleep(1000);
    }

    protected static String authorizationCode, accessToken;

    @BeforeTest(description = "Getting authorization code from redirected web-page", enabled = true)
    public void get1_AuthorizationCode() {
        System.out.println("\t*** Before test actions:\t" + "Get Authorization Code ***");
        //System.out.println(driver.getCurrentUrl());
        Assert.assertTrue(driver.getCurrentUrl().contains(appRedirectUri));
        authorizationCode = driver.getCurrentUrl().split("code=")[1];
        System.out.println("Authorization code is: " + authorizationCode);
    }

    @BeforeTest(description = "Exchange authorization code to access token", enabled = true)
    public void get2_AccessToken() {
        System.out.println("\t*** Before test actions:\t" + "Get Access Token ***");

        RestAssured.baseURI = "https://accounts.spotify.com";
        String response = given().log().all().
                header("Content-Type", "application/x-www-form-urlencoded").
                header("Authorization", "Basic " + Base64coder.encodeToBase64(appClientId, appClientSecret)).
                formParam("grant_type", "authorization_code").
                formParam("code", authorizationCode).
                formParam("redirect_uri", appRedirectUri)
                .when().post("/api/token")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        accessToken = JsonConverter.stringToJson(response).getString("access_token");
        System.out.println("Access Token is: " + accessToken);
    }

    @AfterTest()
    protected void endOfSession() {
        driver.quit();
        System.out.println(driver);
        driver = null;
    }

}
