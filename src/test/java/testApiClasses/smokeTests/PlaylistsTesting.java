package testApiClasses.smokeTests;

import com.google.gson.Gson;
import requestPayloadJson.JsonConverter;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojoClasses.playlistItemsResponse.PlaylistItemsBody;
import pojoClasses.removeSongRequest.CreatorRemoveSongsPojo;
import pojoClasses.removeSongRequest.RemoveSongsBody;
import testApiClasses.parentTestClass.TestBasics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;

public class PlaylistsTesting extends TestBasics {
    protected static String userId, userName, createdPlaylistId, snapshotId;
    protected static int amountOfSongsInPlaylist;

    @Test(description = "Get detailed profile information about current user", priority = 1, enabled = true)
    public static void getCurrentUserProfile() {
        System.out.println("\t*** Test 1 ***\n\t" + "Get User Name and User ID");

        RestAssured.baseURI = "https://api.spotify.com/v1";
        String response = given().log().method().log().uri().
                header("Authorization", "Bearer " + accessToken)
                .when().get("/me")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        userId = JsonConverter.stringToJson(response).getString("id");
        userName = JsonConverter.stringToJson(response).getString("display_name");
        System.out.println("User Name is: " + userName + ", User ID is: " + userId);
    }

    @Test(description = "Create a playlist for a current user", priority = 2, enabled = true)
    public static void createPlaylist() {
        System.out.println("\t*** Test 2 ***\n\t" + "Create new playlist");

        baseURI = "https://api.spotify.com/v1";
        String response = given().log().all().
                header("Authorization", "Bearer " + accessToken).
                header("Content-Type", "application/json").
                body("{\n" +
                        "  \"name\": \"Thrash-Metal playlist API: 2024 " + ((int) (Math.random() * 2024)) + "\",\n" +
                        "  \"description\": \"Playlist was created via REST-Assured API testing library\",\n" +
                        "  \"public\": false,\n" +
                        "  \"collaborative\": false\n" +
                        "}")
                .when().post("/users/" + userId + "/playlists")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();
        createdPlaylistId = JsonConverter.stringToJson(response).getString("id");
        snapshotId = JsonConverter.stringToJson(response).getString("snapshot_id");
        System.out.println("The ID of created playlist is:" + createdPlaylistId);
    }

    @Test(description = "Add one or more items to Playlist. Adding 5 songs into playlist which created in previous method", priority = 3, enabled = true)
    public static void addItemsToPlaylist() throws IOException {
        System.out.println("\t*** Test 3 ***\n\t" + "Add some new songs into created playlist");

        baseURI = "https://api.spotify.com/v1";
        given().log().all().
                header("Authorization", "Bearer " + accessToken).
                header("Content-Type", "application/json").
                body(new String(Files.readAllBytes(Paths.get("src/test/java/requestPayloadJson/payload/addSongs.json"))))
                .when().post("/playlists/" + createdPlaylistId + "/tracks")
                .then().log().all().assertThat().statusCode(200);
    }

    @Test(description = "Change name and description in created playlist", priority = 4, enabled = true)
    public static void changePlaylistDetails() {
        System.out.println("\t*** Test 4 ***\n\t" + "Change some descriptions in created playlist");

        baseURI = "https://api.spotify.com/v1";
        given().log().all().
                header("Authorization", "Bearer " + accessToken).
                header("Content-Type", "application/json").
                pathParam("playlist_id", createdPlaylistId).
                body(
                        "{\n" +
                                "\t\"name\": \"Renamed Metal playlist\",\n" +
                                "\t\"description\": \"Updated via PUT method\",\n" +
                                "\t\"public\": false\n" +
                                "}"
                )
                .when().put("/playlists/{playlist_id}")
                .then().log().all().statusCode(200);

    }

    @Test(description = "Get a list of the playlists of the user", priority = 5, enabled = true)
    public static void getUserPlaylists() {
        System.out.println("\t*** Test 5 ***\n\t" + "Getting information about user's playlists");

        int amountOfPlaylists;

        baseURI = "https://api.spotify.com/v1";
        String response = given().log().all().
                header("Authorization", "Bearer " + accessToken)
                .when().get("/users/" + userId + "/playlists")
                .then().log().status().assertThat().statusCode(200).extract().response().asString();

        amountOfPlaylists = JsonConverter.stringToJson(response).getInt("items.size()");
        System.out.println("Playlists amount of " + userName + ":\t" + amountOfPlaylists);

        for (int i = 0; i < amountOfPlaylists; i++) {
            System.out.println(JsonConverter.stringToJson(response).getString("items[" + i + "].name") + "\tID "
                    + JsonConverter.stringToJson(response).getString("items[" + i + "].id"));
        }
    }

    @Test(description = "Get playlist owned by a user. Show songs from created playlist", priority = 6, enabled = true)
    public static void getPlaylist() {
        System.out.println("\t*** Test 6 ***\n\t" + "Show songs from newly created playlist");

        baseURI = "https://api.spotify.com/v1";
        String response = given().log().all().
                header("Authorization", "Bearer " + accessToken).
                queryParam("fields", "items(track(name,album(name),artists(name)))")
                .when().get("/playlists/" + createdPlaylistId + "/tracks")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        amountOfSongsInPlaylist = JsonConverter.stringToJson(response).getInt("items.size()");
        System.out.println("Amount of songs in current Playlist:\t" + amountOfSongsInPlaylist);
        Assert.assertEquals(amountOfSongsInPlaylist, 5);
    }

    @Test(description = "Add one or more items to Playlist. Adding two more songs to the created playlist", priority = 7, enabled = true)
    public static void addItemsToPlaylistQueryParams() {
        System.out.println("\t*** Test 7 ***\n\t" + "Adding two more songs to the created playlist");

        baseURI = "https://api.spotify.com/v1";
        String response = given().log().all().
                header("Authorization", "Bearer " + accessToken).
                queryParam("position", amountOfSongsInPlaylist).
                queryParam("uris", "spotify:track:3pKsUMqpVr4ZJ13Mm91Xig,spotify:track:5awljpWNO5TpXCyjpvCBbs")
                .when().post("/playlists/" + createdPlaylistId + "/tracks")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        snapshotId = JsonConverter.stringToJson(response).getString("snapshot_id");
    }

    @Test(description = "Get playlist owned by a user. Checking of song amount after adding new songs", priority = 8, enabled = true)
    public static void getPlaylist2() {
        System.out.println("\t*** Test 8 ***\n\t" + "Checking of song amount after adding new songs");

        baseURI = "https://api.spotify.com/v1";
        String response = given().log().all().
                header("Authorization", "Bearer " + accessToken).
                queryParam("fields", "items(track(name,album(name),artists(name)))")
                .when().get("/playlists/" + createdPlaylistId + "/tracks")
                .then().log().status().log().body().assertThat().statusCode(200).extract().response().asString();

        amountOfSongsInPlaylist = JsonConverter.stringToJson(response).getInt("items.size()");
        System.out.println("Amount of songs in current Playlist:\t" + amountOfSongsInPlaylist);
        Assert.assertEquals(amountOfSongsInPlaylist, 7);
    }

    @Test(description = "Remove one or more items using serialized JSON body for query. Delete 3 tracks from created playlist", priority = 9, enabled = true)
    public static void removePlaylistItems() {
        System.out.println("\t*** Test 9 ***\n\t" + "Delete 3 tracks from created playlist");

        baseURI = "https://api.spotify.com/v1";
        RemoveSongsBody removeSongsBody;
        removeSongsBody = CreatorRemoveSongsPojo.createPojo("77xmDupCWuKQ9lz8hSUszC", "3pKsUMqpVr4ZJ13Mm91Xig", "5PFhkQbjJge1h8k7wE1K5U", snapshotId);
        given().log().all().
                header("Authorization", "Bearer " + accessToken).
                header("Content-Type", "application/json").
//          body("{\n" +
//          "    \"tracks\": [\n" +
//          "        {\n" +
//          "            \"uri\": \"spotify:track:5PFhkQbjJge1h8k7wE1K5U\"\n" +
//          "        }\n" +
//          "    ],\n" +
//          "    \"snapshot\": \"" + snapshotId +"\"\n" +
//          "}")
        body(removeSongsBody)
                .when().delete("/playlists/" + createdPlaylistId + "/tracks")
                .then().log().all().assertThat().statusCode(200);

    }

    @Test(description = "Get info from current playlist after deleting tracks. Received response is deserialized into a POJO", priority = 10, enabled = true)
    public static void getPlaylist3() {
        System.out.println("\t*** Test 10 ***\n\t" + "Checking the amount of songs in playlist after all actions");

        baseURI = "https://api.spotify.com/v1";
        PlaylistItemsBody response = given().log().all().
                header("Authorization", "Bearer " + accessToken).
                queryParam("fields", "items(track(name,album(name),artists(name)))").
                expect().defaultParser(Parser.JSON)
                .when().get("/playlists/" + createdPlaylistId + "/tracks").as(PlaylistItemsBody.class);

        Gson gson = new Gson();
        String json = gson.toJson(response);
        JsonConverter.stringToJson(json).prettyPrint();
        amountOfSongsInPlaylist = response.getItems().size();
        System.out.println("Amount of songs in current Playlist:\t" + amountOfSongsInPlaylist);
        Assert.assertEquals(amountOfSongsInPlaylist, 4, "Amount of songs doesn't match expected!!!");
    }

}
