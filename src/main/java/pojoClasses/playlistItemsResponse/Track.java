package pojoClasses.playlistItemsResponse;

import java.util.List;

public class Track {
    private Album album;
    private List<Artists> artists;
    private String name;

    public void setAlbum(Album album){
        this.album = album;
    }

    public void setArtists(List<Artists> artists){
        this.artists = artists;
    }

    public void setName(String name){
        this.name = name;
    }
}
