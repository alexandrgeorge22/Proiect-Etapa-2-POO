package strategies;

public class StrategyFactory {
    public Strategy createStrategy(EnergyChoiceStrategyType strategyType){
        return switch (strategyType) {
            case GREEN -> new GreenStrategy();
            case PRICE -> new PriceStrategy();
            case QUANTITY -> new QuantityStrategy();
        };
    }
}
