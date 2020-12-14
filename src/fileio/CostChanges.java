package fileio;

public final class CostChanges {
    private Integer id;
    private Integer infrastructureCost;
    private Integer productionCost;

    public CostChanges(final Integer id, final Integer infrastructureCost,
                       final Integer productionCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
        this.productionCost = productionCost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
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
