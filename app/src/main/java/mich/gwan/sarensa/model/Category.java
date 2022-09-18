package mich.gwan.sarensa.model;

public class Category {
    int categoryId;
    String categoryName;
    String stationName;

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getStationName() {
        return stationName;
    }
}
