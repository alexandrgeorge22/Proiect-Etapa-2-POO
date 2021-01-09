package fileio;

public final class ProducerFactory {
    private static ProducerFactory instance = null;

    private ProducerFactory() {

    }

    /**
     * @return ProducerFactory instance
     */
    public static ProducerFactory getInstance() {
        if (instance == null) {
            instance = new ProducerFactory();
        }
        return instance;
    }

    /**
     * @param id                   of new producer
     * @param energyType           of new producer
     * @param maxDistributors      of new producer
     * @param priceKW              for new producer
     * @param energyPerDistributor for new producer
     * @return a new producer
     */
    public Producer createProducer(final Integer id, final String energyType,
                                   final Integer maxDistributors, final Float priceKW,
                                   final Integer energyPerDistributor) {
        return new Producer(id, energyType, maxDistributors, priceKW, energyPerDistributor);
    }
}
