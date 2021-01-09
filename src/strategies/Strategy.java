package strategies;

import fileio.Distributor;
import fileio.Producer;

import java.util.List;

public interface Strategy {
    /**
     * @param distributor who apply the strategy
     * @param producers   list needed to apply the strategy
     */
    void applyStrategy(Distributor distributor, List<Producer> producers);
}
