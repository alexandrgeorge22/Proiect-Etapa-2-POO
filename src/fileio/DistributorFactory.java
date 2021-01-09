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
     * @param id                 of new distributor
     * @param contractLength     for new distributor
     * @param budget             of new distributor
     * @param infrastructureCost of new distributor
     * @param energyNeededKW     of new distributor
     * @param producerStrategy   of new distributor
     * @return a new distributor
     */
    public Distributor createDistributor(final Integer id, final Integer contractLength,
                                         final Integer budget, final Integer infrastructureCost,
                                         final Integer energyNeededKW,
                                         final String producerStrategy) {
        return new Distributor(id, contractLength,
                budget, infrastructureCost,
                energyNeededKW, producerStrategy);
    }
}
