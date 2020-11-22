package output;

import java.util.List;

public class Solution {

    private double cost;
    private List<Route> routes;

    public Solution(List<Route> routes) {
        this.routes = routes;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    private void evaluate(){

    }

    private void validate(){

    }
}
