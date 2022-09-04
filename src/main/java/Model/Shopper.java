package Model;

import java.util.List;

public class Shopper {
    private String shopperId;
    private List<Shelf> shelf;

    public String getShopperId() {
        return shopperId;
    }

    public void setShopperId(String shopperId) {
        this.shopperId = shopperId;
    }

    public List<Shelf> getShelf() {
        return shelf;
    }

    public void setShelf(List<Shelf> shelf) {
        this.shelf = shelf;
    }

}
