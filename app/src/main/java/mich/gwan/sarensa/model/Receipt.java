package mich.gwan.sarensa.model;

public class Receipt {
    private int receiptId;
    private String receiptDate;
    private String receiptTime;
    private String receiptTransType;
    private String stationName;
    private String itemName;
    private String itemCategory;
    private String customerName;
    private int itemQuantity;
    private int sellingPrice;
    private int total;

    public Receipt() {
    }

    public Receipt(String receiptDate, String receiptTime, String receiptTransType, String customerName,
                   String stationName, String itemCategory, String itemName, int itemQuantity, int sellingPrice, int total) {
        this.receiptDate = receiptDate;
        this.receiptTime = receiptTime;
        this.receiptTransType = receiptTransType;
        this.stationName = stationName;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.customerName = customerName;
        this.itemQuantity = itemQuantity;
        this.sellingPrice = sellingPrice;
        this.total = total;
    }

    public int getReceiptId() {
        return receiptId;
    }
    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }
    public String getReceiptDate() {
        return receiptDate;
    }
    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }
    public String getReceiptTime() {
        return receiptTime;
    }
    public void setReceiptTime(String receiptTime) {
        this.receiptTime = receiptTime;
    }
    public String getReceiptTransType() {
        return receiptTransType;
    }
    public void setReceiptTransType(String receiptTransType) {
        this.receiptTransType = receiptTransType;
    }
    public String getStationName() {
        return stationName;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getItemCategory() {
        return itemCategory;
    }
    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public int getItemQuantity() {
        return itemQuantity;
    }
    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
    public int getSellingPrice() {
        return sellingPrice;
    }
    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}
