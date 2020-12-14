package fileio;

import java.util.List;

public final class MonthlyUpdate {
    private List<Consumer> newConsumers;
    private List<CostChanges> costChanges;

    public MonthlyUpdate(final List<Consumer> newConsumers,
                         final List<CostChanges> costChanges) {
        this.newConsumers = newConsumers;
        this.costChanges = costChanges;
    }

    public List<Consumer> getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final List<Consumer> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public List<CostChanges> getCostChanges() {
        return costChanges;
    }

    public void setCostChanges(final List<CostChanges> costChanges) {
        this.costChanges = costChanges;
    }

}
