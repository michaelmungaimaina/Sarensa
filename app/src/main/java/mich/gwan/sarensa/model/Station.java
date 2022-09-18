package mich.gwan.sarensa.model;

public class Station {
    int stationId;
    String stationName;
    String location;

    public void setName(String name) {
        this.stationName = name;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStationId() {
        return stationId;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return stationName;
    }
}
