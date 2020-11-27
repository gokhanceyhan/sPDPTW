package algorithms;

public class HeuristicStatistics {

    private int numTimesUsed;
    private int numTimesWithNewGlobalBestSolution;
    private int numTimesWithLocallyImprovedSolution;
    private int numTimesWithAcceptedSolution;
    private double score;

    public HeuristicStatistics() {
    }

    public HeuristicStatistics(
            int numTimesUsed, int numTimesWithNewGlobalBestSolution, int numTimesWithLocallyImprovedSolution,
            int numTimesWithAcceptedSolution, double score) {
        this.numTimesUsed = numTimesUsed;
        this.numTimesWithNewGlobalBestSolution = numTimesWithNewGlobalBestSolution;
        this.numTimesWithLocallyImprovedSolution = numTimesWithLocallyImprovedSolution;
        this.numTimesWithAcceptedSolution = numTimesWithAcceptedSolution;
        this.score = score;
    }

    public void incrementNumTimeUsed(){
        this.setNumTimesUsed(this.getNumTimesUsed() + 1);
    }

    public void incrementNumTimesWithAcceptedSolution(){
        this.setNumTimesWithAcceptedSolution(this.getNumTimesWithAcceptedSolution() + 1);
    }

    public void incrementNumTimesWithLocallyImprovedSolution(){
        this.setNumTimesWithLocallyImprovedSolution(this.getNumTimesWithLocallyImprovedSolution() + 1);
    }

    public void incrementNumTimesWithNewGlobalBestSolution(){
        this.setNumTimesWithNewGlobalBestSolution(this.getNumTimesWithNewGlobalBestSolution() + 1);
    }

    public int getNumTimesUsed() {
        return numTimesUsed;
    }

    public void setNumTimesUsed(int numTimesUsed) {
        this.numTimesUsed = numTimesUsed;
    }

    public int getNumTimesWithNewGlobalBestSolution() {
        return numTimesWithNewGlobalBestSolution;
    }

    public void setNumTimesWithNewGlobalBestSolution(int numTimesWithNewGlobalBestSolution) {
        this.numTimesWithNewGlobalBestSolution = numTimesWithNewGlobalBestSolution;
    }

    public int getNumTimesWithLocallyImprovedSolution() {
        return numTimesWithLocallyImprovedSolution;
    }

    public void setNumTimesWithLocallyImprovedSolution(int numTimesWithLocallyImprovedSolution) {
        this.numTimesWithLocallyImprovedSolution = numTimesWithLocallyImprovedSolution;
    }

    public int getNumTimesWithAcceptedSolution() {
        return numTimesWithAcceptedSolution;
    }

    public void setNumTimesWithAcceptedSolution(int numTimesWithAcceptedSolution) {
        this.numTimesWithAcceptedSolution = numTimesWithAcceptedSolution;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setScore(HeuristicScoringFunction heuristicScoringFunction){
        this.score = heuristicScoringFunction.score(this);
    }
}
