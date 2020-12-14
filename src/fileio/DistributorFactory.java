package fileio;

public final class DistributorFactory {
    private static DistributorFactory instance = null;

    private DistributorFactory() {
    }

    /**
     * @return DistributorFactory instance
     */
    public static DistributorFactory getInstance() {
        if (instance == null) {
            instance = new DistributorFactory();
        }
        return instance;
    }

    /**
     * @param id                        of distributor
     * @param contractLength            for this distributor
     * @param budget                    of distributor
     * @param initialInfrastructureCost for distributor
     * @param initialProductionCost     for distributor
     * @return a new distributor
     */
    public Distributor createDistributor(final Integer id, final Integer contractLength,
                                         final Integer budget,
                                         final Integer initialInfrastructureCost,
                                         final Integer initialProductionCost) {
        return new Distributor(id, contractLength,
                budget, initialInfrastructureCost,
                initialProductionCost);
    }
}
