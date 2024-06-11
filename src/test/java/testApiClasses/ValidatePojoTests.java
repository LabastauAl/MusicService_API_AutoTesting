package testApiClasses;

import com.google.gson.Gson;
import requestPayloadJson.JsonConverter;


import org.testng.annotations.Test;
import pojoClasses.playlistItemsResponse.CreatorPlaylistItemsPojo;
import pojoClasses.playlistItemsResponse.PlaylistItemsBody;
import pojoClasses.removeSongRequest.CreatorRemoveSongsPojo;
import pojoClasses.removeSongRequest.RemoveSongsBody;

public class ValidatePojoTests {
    @Test(description = "Validation of serialization to JSON from fixed 3 songs IDs parameters", priority = 1, enabled = true)
    void checkingRemoveRequestPojoVer1() {
        RemoveSongsBody removeSongsBody;
        removeSongsBody = CreatorRemoveSongsPojo.createPojo("Song_1_ID", "Song_2_ID", "Song_3_ID", "Playlist_Snapshot_ID_1");

        Gson gson = new Gson();
        String json = gson.toJson(removeSongsBody);
        JsonConverter.stringToJson(json).prettyPrint();
    }

    @Test(description = "Validation of serialisation to JSON from array of songs IDs parameters", priority = 2, enabled = true)
    void checkingRemoveRequestPojoVer2() {
        String[] songIDs = {"Song_4_ID", "Song_5_ID", "Song_6_ID", "Song_7_ID"};
        String snapshotId = "Playlist_Snapshot_ID_2";
        RemoveSongsBody removeSongsBody;
        removeSongsBody = CreatorRemoveSongsPojo.createPojo(songIDs, snapshotId);

        Gson gson = new Gson();
        String json = gson.toJson(removeSongsBody);
        JsonConverter.stringToJson(json).prettyPrint();
    }

    @Test(priority = 3, enabled = true)
    void createRequestPojo() {
        String[][] songItems = new String[4][];
        songItems[0] = new String[]{"Am I Evil?", "The Big 4", "Metallica", "Anthrax", "Megadeth", "Slayer"};
        songItems[1] = new String[]{"While We Sleep", "Shadows Of The Dying Sun", "Insomnium"};
        songItems[2] = new String[]{"State Of Unrest", "State Of Unrest", "Lamb Of God", "Kreator"};
        songItems[3] = new String[]{"Saxons And Vikings", "The Great Heathen Army", "Amon Amarth", "Saxon"};

        PlaylistItemsBody playlistItemsBody;
        playlistItemsBody = CreatorPlaylistItemsPojo.createPojo(songItems);
        Gson gson = new Gson();
        String json = gson.toJson(playlistItemsBody);
        JsonConverter.stringToJson(json).prettyPrint();
    }
}
