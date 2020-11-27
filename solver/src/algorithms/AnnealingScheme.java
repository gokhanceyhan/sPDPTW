package algorithms;

public class AnnealingScheme {

    private double coolingRate;
    private double currentTemperature;
    private double startTemperature;

    public AnnealingScheme(double coolingRate, double startTemperature) {
        this.coolingRate = coolingRate;
        this.currentTemperature = startTemperature;
        this.startTemperature = startTemperature;
    }

    public double calculateAcceptProbability(double currentSolutionCost, double candidateSolutionCost){
        return Math.exp(-1 * (candidateSolutionCost - currentSolutionCost) / this.getCurrentTemperature());
    }

    public void updateTemperature(){
        this.setCurrentTemperature(this.getCurrentTemperature() * this.getCoolingRate());
    }

    public double getCoolingRate() {
        return coolingRate;
    }

    public void setCoolingRate(double coolingRate) {
        this.coolingRate = coolingRate;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public double getStartTemperature() {
        return startTemperature;
    }

    public void setStartTemperature(double startTemperature) {
        this.startTemperature = startTemperature;
    }
}
