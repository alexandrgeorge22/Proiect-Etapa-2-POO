package strategies;

import fileio.Distributor;
import fileio.Producer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PriceStrategy implements Strategy {

    static class PriceSort implements Comparator<Producer> {
        @Override
        public int compare(final Producer o1, final Producer o2) {
            if (o1.getPriceKW().compareTo(o2.getPriceKW()) == 0) {
                return o1.getEnergyPerDistributor().compareTo(o2.getEnergyPerDistributor());
            }
            return o1.getPriceKW().compareTo(o2.getPriceKW());
        }
    }

    @Override
    public void applyStrategy(Distributor distributor, List<Producer> producers) {
        System.out.println(producers);
        Collections.sort(producers, new PriceSort());
        System.out.println(producers);
    }
}
