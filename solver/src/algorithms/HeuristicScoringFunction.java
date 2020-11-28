package algorithms;

public class HeuristicScoringFunction {

    private double rewardForAcceptedSolution;
    private double rewardForLocallyImprovedSolution;
    private double rewardForNewGlobalBestSolution;

    public HeuristicScoringFunction(
            double rewardForAcceptedSolution, double rewardForLocallyImprovedSolution,
            double rewardForNewGlobalBestSolution) {
        this.rewardForAcceptedSolution = rewardForAcceptedSolution;
        this.rewardForLocallyImprovedSolution = rewardForLocallyImprovedSolution;
        this.rewardForNewGlobalBestSolution = rewardForNewGlobalBestSolution;
    }

    public double score (HeuristicStatistics heuristicStatistics){
        return this.getRewardForNewGlobalBestSolution() * heuristicStatistics.getNumTimesWithNewGlobalBestSolution() +
                this.getRewardForLocallyImprovedSolution() *
                        heuristicStatistics.getNumTimesWithLocallyImprovedSolution() +
                this.getRewardForAcceptedSolution() * heuristicStatistics.getNumTimesWithAcceptedSolution();
    }

    public double getRewardForNewGlobalBestSolution() {
        return rewardForNewGlobalBestSolution;
    }

    public void setRewardForNewGlobalBestSolution(double rewardForNewGlobalBestSolution) {
        this.rewardForNewGlobalBestSolution = rewardForNewGlobalBestSolution;
    }

    public double getRewardForLocallyImprovedSolution() {
        return rewardForLocallyImprovedSolution;
    }

    public void setRewardForLocallyImprovedSolution(double rewardForLocallyImprovedSolution) {
        this.rewardForLocallyImprovedSolution = rewardForLocallyImprovedSolution;
    }

    public double getRewardForAcceptedSolution() {
        return rewardForAcceptedSolution;
    }

    public void setRewardForAcceptedSolution(double rewardForAcceptedSolution) {
        this.rewardForAcceptedSolution = rewardForAcceptedSolution;
    }
}
