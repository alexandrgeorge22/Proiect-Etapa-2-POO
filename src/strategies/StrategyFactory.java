package strategies;

public final class StrategyFactory {
    private static StrategyFactory instance = null;

    private StrategyFactory() {

    }

    /**
     * @return StrategyFactory instance
     */
    public static StrategyFactory getInstance() {
        if (instance == null) {
            instance = new StrategyFactory();
        }
        return instance;
    }

    /**
     * @param strategyType used to create a specific strategy
     * @return a specific strategy
     */
    public Strategy createStrategy(EnergyChoiceStrategyType strategyType) {
        return switch (strategyType) {
            case GREEN -> new GreenStrategy();
            case PRICE -> new PriceStrategy();
            case QUANTITY -> new QuantityStrategy();
        };
    }
}
