package fileio;

public final class ProducerFactory {
    private static ProducerFactory instance = null;

    private ProducerFactory() {

    }

    public static ProducerFactory getInstance() {
        if (instance == null) {
            instance = new ProducerFactory();
        }
        return instance;
    }

    public Producer createProducer(final Integer id, final String energyType,
                                   final Integer maxDistributors, final Float priceKW,
                                   final Integer energyPerDistributor) {
        return new Producer(id, energyType, maxDistributors, priceKW, energyPerDistributor);
    }
}
