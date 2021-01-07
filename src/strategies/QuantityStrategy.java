package strategies;

import fileio.Distributor;
import fileio.Producer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuantityStrategy implements Strategy {
    static class QuantitySort implements Comparator<Producer> {
        @Override
        public int compare(final Producer o1, final Producer o2) {
            return o1.getEnergyPerDistributor().compareTo(o2.getEnergyPerDistributor());
        }
    }

    @Override
    public void applyStrategy(Distributor distributor, List<Producer> producers) {
        System.out.println(producers);
        Collections.sort(producers, new QuantitySort());
        System.out.println(producers);
    }
}
