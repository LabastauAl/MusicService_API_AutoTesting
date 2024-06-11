package pojoClasses.removeSongRequest;

import java.util.List;

public class RemoveSongsBody {
    private List<Tracks> tracks;
    private String snapshot;

    public void setTracks(List<Tracks> tracks) {
        this.tracks = tracks;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }
}
