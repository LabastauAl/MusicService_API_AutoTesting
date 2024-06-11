package pojoClasses.playlistItemsResponse;

import java.util.ArrayList;
import java.util.List;

public class CreatorPlaylistItemsPojo {
    public static PlaylistItemsBody createPojo(String[][] songItems) {
        PlaylistItemsBody playlistItemsBody = new PlaylistItemsBody();
        List<Items> items = new ArrayList<>();


        for (int i = 0; i < songItems.length; i++) {
            Items item = new Items();
            Track track = new Track();
            Album album = new Album();

            List<Artists> artistsList = new ArrayList<>();
            track.setName(songItems[i][0]);
            album.setName(songItems[i][1]);
            for (int j = 2; j < songItems[i].length; j++) {
                Artists artists = new Artists();
                artists.setName(songItems[i][j]);
                artistsList.add(artists);
            }
            track.setArtists(artistsList);
            track.setAlbum(album);

            item.setTracks(track);
            items.add(i, item);
        }
        playlistItemsBody.setItems(items);
        return playlistItemsBody;
    }
}

