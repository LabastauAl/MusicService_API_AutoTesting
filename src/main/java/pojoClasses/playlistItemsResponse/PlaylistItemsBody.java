package pojoClasses.playlistItemsResponse;

import java.util.List;

public class PlaylistItemsBody {
    private List<Items> items;

    public void setItems(List<Items> items) {
        this.items = items;
    }
    public List<Items> getItems(){
        return items;
    }
}
