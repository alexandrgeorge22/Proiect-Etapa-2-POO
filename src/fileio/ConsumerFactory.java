package fileio;

public final class ConsumerFactory {
    private static ConsumerFactory instance = null;

    private ConsumerFactory() {
    }

    /**
     * @return ConsumerFactory instance
     */
    public static ConsumerFactory getInstance() {
        if (instance == null) {
            instance = new ConsumerFactory();
        }
        return instance;
    }

    /**
     * @param id            of consumer
     * @param initialBudget of consumer
     * @param monthlyIncome of consumer
     * @return a new consumer
     */
    public Consumer createConsumer(final Integer id, final Integer initialBudget,
                                   final Integer monthlyIncome) {
        return new Consumer(id, initialBudget, monthlyIncome);
    }
}
