package fileio;

import java.util.List;

public final class Input {
    private final Integer numberofTurns;
    private final List<Consumer> consumersData;
    private final List<Distributor> distributorsData;
    private final List<MonthlyUpdate> monthlyUpdatesData;

    public Input() {
        this.numberofTurns = null;
        this.consumersData = null;
        this.distributorsData = null;
        this.monthlyUpdatesData = null;
    }


    public Input(final Integer numberofTurns,
                 final List<Consumer> consumersData,
                 final List<Distributor> distributorsData,
                 final List<MonthlyUpdate> monthlyUpdatesData) {
        this.numberofTurns = numberofTurns;
        this.consumersData = consumersData;
        this.distributorsData = distributorsData;
        this.monthlyUpdatesData = monthlyUpdatesData;
    }

    public Integer getNumberofTurns() {
        return numberofTurns;
    }

    public List<Consumer> getConsumersData() {
        return consumersData;
    }

    public List<Distributor> getDistributorsData() {
        return distributorsData;
    }

    public List<MonthlyUpdate> getMonthlyUpdatesData() {
        return monthlyUpdatesData;
    }
}
