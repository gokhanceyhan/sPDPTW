package algorithms;

public class LocalSearchResult {

    private InsertionHeuristicType insertionHeuristicType;
    private RemovalHeuristicType removalHeuristicType;
    private boolean newGlobalBestSolution;
    private boolean locallyImprovedSolution;
    private boolean newSolution;

    public LocalSearchResult(
            InsertionHeuristicType insertionHeuristicType, RemovalHeuristicType removalHeuristicType,
            boolean newGlobalBestSolution, boolean locallyImprovedSolution, boolean newSolution) {
        this.insertionHeuristicType = insertionHeuristicType;
        this.removalHeuristicType = removalHeuristicType;
        this.newGlobalBestSolution = newGlobalBestSolution;
        this.locallyImprovedSolution = locallyImprovedSolution;
        this.newSolution = newSolution;
    }

    public InsertionHeuristicType getInsertionHeuristicType() {
        return insertionHeuristicType;
    }

    public void setInsertionHeuristicType(InsertionHeuristicType insertionHeuristicType) {
        this.insertionHeuristicType = insertionHeuristicType;
    }

    public RemovalHeuristicType getRemovalHeuristicType() {
        return removalHeuristicType;
    }

    public void setRemovalHeuristicType(RemovalHeuristicType removalHeuristicType) {
        this.removalHeuristicType = removalHeuristicType;
    }

    public boolean isNewGlobalBestSolution() {
        return newGlobalBestSolution;
    }

    public void setNewGlobalBestSolution(boolean newGlobalBestSolution) {
        this.newGlobalBestSolution = newGlobalBestSolution;
    }

    public boolean isLocallyImprovedSolution() {
        return locallyImprovedSolution;
    }

    public void setLocallyImprovedSolution(boolean locallyImprovedSolution) {
        this.locallyImprovedSolution = locallyImprovedSolution;
    }

    public boolean isNewSolution() {
        return newSolution;
    }

    public void setNewSolution(boolean newSolution) {
        this.newSolution = newSolution;
    }
}
