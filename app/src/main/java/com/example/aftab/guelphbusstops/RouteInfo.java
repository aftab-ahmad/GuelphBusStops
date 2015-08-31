package com.example.aftab.guelphbusstops;

public class RouteInfo {

    private String title;
    private String routeName;
    private String routeDescription;
    private String available;

    public RouteInfo(String title, String routeName, String routeDescription) {

        this.title = title;
        this.routeName = routeName;
        this.routeDescription = routeDescription;
    }

    public RouteInfo() {
        // TODO Auto-generated constructor stub
    }

    public String getTitle () {
        return title;
    }
    public String getRouteName () {
        return routeName;
    }
    public String getRouteDescription () {
        return routeDescription;
    }
    public String getAvailable () {
        return available;
    }

    public void setTitle (String title) {
        this.title = title;
    }
    public void setRouteName (String routeName) {
        this.routeName = routeName;
    }
    public void setAvailable (String available) {
        this.available = available;
    }
    public void setRouteDescription (String routeDescription) {
        this.routeDescription = routeDescription;
    }
}
