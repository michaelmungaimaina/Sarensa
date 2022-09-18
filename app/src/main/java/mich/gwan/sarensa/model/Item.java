package mich.gwan.sarensa.model;

public class Item {
    int itemId;
    String categoryName;
    String StationName;
    String itemName;
    int itemQnty;
    int buyPrice;
    int sellPrice;

    public int getItemId() {
        return itemId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getStationName() {
        return StationName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemQnty() {
        return itemQnty;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemQnty(int itemQnty) {
        this.itemQnty = itemQnty;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }
}
