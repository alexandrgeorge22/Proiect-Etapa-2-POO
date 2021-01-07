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


    public Distributor createDistributor(final Integer id, final Integer contractLength,
                                         final Integer budget, final Integer infrastructureCost,
                                         final Integer energyNeededKW, final String producerStrategy) {
        return new Distributor(id, contractLength,
                budget, infrastructureCost,
                energyNeededKW,producerStrategy);
    }
}
