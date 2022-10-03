package mich.gwan.sarensa.model;

public class TempCart {
    String categoryName;
    String itemName;
    int itemQnty;
    int buyPrice;
    int sellPrice;

    public String getCategoryName() {
        return categoryName;
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
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
