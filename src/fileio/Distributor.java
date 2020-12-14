package fileio;

public final class Distributor {
    private Integer id;
    private Integer contractLength;
    private Integer budget;
    private Integer infrastructureCost;
    private Integer productionCost;
    private Integer contractPrice;
    private Integer contractedConsumers = 0;
    private boolean isBankrupt = false;

    public Distributor(final Integer id, final Integer contractLength,
                       final Integer budget,
                       final Integer infrastructureCost,
                       final Integer productionCost) {
        this.id = id;
        this.contractLength = contractLength;
        this.budget = budget;
        this.infrastructureCost = infrastructureCost;
        this.productionCost = productionCost;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public Integer getContractedConsumers() {
        return contractedConsumers;
    }

    public void setContractedConsumers(final Integer contractedConsumers) {
        this.contractedConsumers = contractedConsumers;
    }

    public Integer getContractPrice() {
        return contractPrice;
    }

    /**
     * @param contractedConsumers of distributor used to calculate the contract price
     */
    public void setContractPrice(Integer contractedConsumers) {
        int infinity = 999999999;
        if (contractedConsumers == null) {
            this.contractPrice = infinity;
        } else {
            if (contractedConsumers.equals(0)) {
                contractedConsumers = 1;
            }
            Integer profit = Math.toIntExact(Math.round(Math.floor(0.2 * this.productionCost)));
            this.contractPrice = Math.toIntExact(Math.round(Math.floor(
                    this.infrastructureCost / contractedConsumers) + this.productionCost + profit));
        }

    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(final Integer budget) {
        this.budget = budget;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getContractLength() {
        return contractLength;
    }

    public void setContractLength(final Integer contractLength) {
        this.contractLength = contractLength;
    }

    public Integer getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final Integer infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public Integer getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(final Integer productionCost) {
        this.productionCost = productionCost;
    }
}
