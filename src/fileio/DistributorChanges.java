package fileio;

public final class DistributorChanges {
    private Integer id;
    private Integer infrastructureCost;

    public DistributorChanges(final Integer id, final Integer infrastructureCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(Integer infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }
}
