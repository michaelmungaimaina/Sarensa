package mich.gwan.sarensa.model;

public class Sales {
    int saleId;
    String date;
    String time;
    String saleType;
    String stationName;
    String itemName;
    int itemQnty;
    int sellPrice;
    int profit;
    int total;

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public void setItemQnty(int itemQnty) {
        this.itemQnty = itemQnty;
    }
    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }
    public void setProfit(int profit) {
        this.profit = profit;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    public int getSaleId() {
        return saleId;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getSaleType() {
        return saleType;
    }
    public String getStationName() {
        return stationName;
    }
    public String getItemName() {
        return itemName;
    }
    public int getItemQnty() {
        return itemQnty;
    }
    public int getSellPrice() {
        return sellPrice;
    }
    public int getProfit() {
        return profit;
    }
    public int getTotal() {
        return total;
    }
}
