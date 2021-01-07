package fileio;

public final class Consumer {
    private Integer id;
    private Integer budget;
    private Integer monthlyIncome;
    private Integer penalty = null;
    private Integer actualContractLength = 0;
    private Integer distributor = null;
    private Integer contractPrice = null;
    private boolean isBankrupt = false;


    public Consumer(final Integer id, final Integer initialBudget,
                    final Integer monthlyIncome) {
        this.id = id;
        this.budget = initialBudget;
        this.monthlyIncome = monthlyIncome;
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "id=" + id +
                ", budget=" + budget +
                ", monthlyIncome=" + monthlyIncome +
                ", penalty=" + penalty +
                ", actualContractLength=" + actualContractLength +
                ", distributor=" + distributor +
                ", contractPrice=" + contractPrice +
                ", isBankrupt=" + isBankrupt +
                '}';
    }

    public Integer getPenalty() {
        return penalty;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public Integer getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(final Integer contractPrice) {
        this.contractPrice = contractPrice;
    }

    public void setPenalty(final Integer penalty) {
        this.penalty = penalty;
    }

    public Integer getActualContractLength() {
        return actualContractLength;
    }

    public void setActualContractLength(final Integer actualContractLength) {
        this.actualContractLength = actualContractLength;
    }

    public Integer getDistributor() {
        return distributor;
    }

    public void setDistributor(final Integer distributor) {
        this.distributor = distributor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(final Integer budget) {
        this.budget = budget;
    }

    public Integer getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(final Integer monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
}
