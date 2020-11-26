package common;

public class ScalingFunction {

    private double max;
    private double min;

    public ScalingFunction(double max, double min) {
        this.max = max;
        this.min = min;
    }

    public double scale(double value){
        Double range = this.getMax() - this.getMin();
        return range.equals(0.0) ? 0.0 : value / range;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }
}
