package testApiClasses.smokeTests;

import requestPayloadJson.JsonConverter;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.Test;
import testApiClasses.parentTestClass.TestBasics;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class NegativeScenarios extends TestBasics {
    @Test(description = "Trying to get information about current user by sending a request to wrong endpoint", priority = 1, enabled = true)
    public static void sendQueryToWrongEndpoint() {
        System.out.println("\t*** Test 1 ***\n\t" + "Sending request to wrong endpoint");

        RestAssured.baseURI = "https://api.spotify.com/v1";
        String response = given().log().method().log().uri().
                header("Authorization", "Bearer " + accessToken)
                .when().get("/mee")
                .then().log().status().log().body().assertThat().statusCode(404).extract().response().asString();

        String errorMessage = JsonConverter.stringToJson(response).getString("error.message");
        Assert.assertEquals(errorMessage, "Service not found");
    }

    @Test(description = "Trying to get information about songs in playlist with wrong playlist Id", priority = 2, enabled = true)
    public static void getWrongPlaylist() {
        System.out.println("\t*** Test 2 ***\n\t" + "Sending request with wrong playlist ID endpoint");

        String wrongPlaylistId = "123SomeWrongPlaylistId";

        baseURI = "https://api.spotify.com/v1";
        String response = given().log().method().log().uri().log().params().
                header("Authorization", "Bearer " + accessToken).
                queryParam("fields", "items(track(name,album(name)))")
                .when().get("/playlists/" + wrongPlaylistId + "/tracks")
                .then().log().status().log().body().assertThat().statusCode(502).extract().response().asString();

        String errorMessage = JsonConverter.stringToJson(response).getString("error.message");
        Assert.assertEquals(errorMessage, "Error while loading resource");
    }

    @Test(description = "Attempt to add some songs to the private playlist from another user", priority = 3, enabled = true)
    public static void addSongsToPrivatePlaylist() {
        System.out.println("\t*** Test 3 ***\n\t" + "Sending request to trying to change another user's playlist");

        String privatePlaylistId = "3cEYpjA9oz9GiPac4AsH4n"; // private playlist title "Spotify Web API Testing playlist"

        baseURI = "https://api.spotify.com/v1";
        String response = given().log().method().log().parameters().
                header("Authorization", "Bearer " + accessToken).
                queryParam("uris", "spotify:track:3pKsUMqpVr4ZJ13Mm91Xig,spotify:track:5awljpWNO5TpXCyjpvCBbs")
                .when().post("/playlists/" + privatePlaylistId + "/tracks")
                .then().log().status().log().body().assertThat().statusCode(403).extract().response().asString();

        String errorMessage = JsonConverter.stringToJson(response).getString("error.message");
        Assert.assertEquals(errorMessage, "Forbidden");
    }

    @Test(description = "Attempt to change some details to the standard Spotify playlist", priority = 4, enabled = true)
    public static void changeSpotifyPlaylistDetails(){
        System.out.println("\t*** Test 4 ***\n\t" + "Sending request to trying to change Spotify's playlist details");

        String spotifyPlaylistId = "37i9dQZF1DWXRqgorJj26U"; //standard Spotify playlist title "Rock Classics"

        baseURI = "https://api.spotify.com/v1";
        String response = given().log().method().log().uri().log().headers().log().body().
                header("Authorization", "Bearer " + accessToken).
                header("Content-Type", "application/json").
                body(
                        "{\n" +
                                "\t\"name\": \"Changed title\",\n" +
                                "\t\"description\": \"Trying to change details in private playlist\",\n" +
                                "\t\"public\": false\n" +
                                "}"
                )
                .when().put("/playlists/" + spotifyPlaylistId)
                .then().log().status().log().body().assertThat().statusCode(403).extract().response().asString();

        String errorMessage = JsonConverter.stringToJson(response).getString("error.message");
        Assert.assertEquals(errorMessage, "Not allowed");
    }

    @Test(description = "Attempt to add two more songs with wrong IDs to the existing playlist", priority = 5, enabled = true)
    public static void addSongsWithWrongID() {
        System.out.println("\t*** Test 5 ***\n\t" + "Sending request with wrong songs IDs");

        String songToAddId1 = "3pKsUMqpVr4ZJ13Mm91Xig";
        String songToAddId2 = "3pKsUMqpVr4ZJ13Mm91Xi";

        baseURI = "https://api.spotify.com/v1";
        String userIdResponse = given().log().method().log().headers().
                header("Authorization", "Bearer " + accessToken)
                .when().get("/me")
                .then().assertThat().statusCode(200).extract().response().asString();
        String userId = JsonConverter.stringToJson(userIdResponse).getString("id");
        String userName = JsonConverter.stringToJson(userIdResponse).getString("display_name");
        System.out.println("User Name is: " + userName + ", User ID is: " + userId);

        String playlistsResponse = given().log().method().log().headers().
                header("Authorization", "Bearer " + accessToken)
                .when().get("/users/" + userId + "/playlists")
                .then().extract().response().asString();
        String playlistId = JsonConverter.stringToJson(playlistsResponse).getString("items[0].id");
        System.out.println("The ID of testing playlist:\t" + playlistId);

        String response = given().log().uri().log().method().log().parameters().log().headers().
                header("Authorization", "Bearer " + accessToken).
                queryParam("position", 0).
                queryParam("uris", "spotify:track:" + songToAddId1 + ",spotify:track:" + songToAddId2)
                .when().post("/playlists/" + playlistId + "/tracks")
                .then().log().status().log().body().assertThat().statusCode(400).extract().response().asString();

        String errorMessage = JsonConverter.stringToJson(response).getString("error.message");
        Assert.assertEquals(errorMessage, "Invalid base62 id");
    }
}
