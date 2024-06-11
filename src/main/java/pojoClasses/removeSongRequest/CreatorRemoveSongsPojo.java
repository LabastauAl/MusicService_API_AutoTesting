package pojoClasses.removeSongRequest;

import java.util.ArrayList;
import java.util.List;

public class CreatorRemoveSongsPojo {
    public static RemoveSongsBody createPojo(String song1Id, String song2Id, String song3Id, String playlistSnapshotId){
        Tracks track1Uri = new Tracks();
        Tracks track2Uri = new Tracks();
        Tracks track3Uri = new Tracks();

        List<Tracks> tracks = new ArrayList<>();
        RemoveSongsBody removeSongsBody = new RemoveSongsBody();

        track1Uri.setUri(song1Id);
        track2Uri.setUri(song2Id);
        track3Uri.setUri(song3Id);
        tracks.add(0, track1Uri);
        tracks.add(1, track2Uri);
        tracks.add(2, track3Uri);

        removeSongsBody.setTracks(tracks);
        removeSongsBody.setSnapshot(playlistSnapshotId);
        return removeSongsBody;
    }

    public static RemoveSongsBody createPojo(String[] songsId, String playlistSnapshotId){
        RemoveSongsBody removeSongsBody = new RemoveSongsBody();
        List<Tracks> tracks = new ArrayList<>();

        for(int i = 0; i < songsId.length; i++){
            Tracks trackUri = new Tracks();
            trackUri.setUri(songsId[i]);
            tracks.add(i, trackUri);
        }
        removeSongsBody.setTracks(tracks);
        removeSongsBody.setSnapshot(playlistSnapshotId);

        return removeSongsBody;
    }
}
