package pojoClasses.removeSongRequest;

public class Tracks {
    private String uri;

    public void setUri(String songID) {
        uri = "spotify:track:" + songID;
    }
}
