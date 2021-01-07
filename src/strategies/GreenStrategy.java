package strategies;

import entities.EnergyType;
import fileio.Distributor;
import fileio.Producer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GreenStrategy implements Strategy {

    static class GreenSort implements Comparator<Producer> {
        @Override
        public int compare(final Producer o1, final Producer o2) {
            if (o1.getEnergyType().isRenewable() && o2.getEnergyType().isRenewable()) {
                if (o1.getPriceKW().compareTo(o2.getPriceKW()) == 0) {
                    return o1.getEnergyPerDistributor().compareTo(o2.getEnergyPerDistributor());
                }
                return o1.getPriceKW().compareTo(o2.getPriceKW());
            } else if (o1.getEnergyType().isRenewable() && !o2.getEnergyType().isRenewable()) {
                return -1;
            } else
                return 1;
        }
    }

    @Override
    public void applyStrategy(Distributor distributor, List<Producer> producers) {
        System.out.println(producers);
        Collections.sort(producers, new GreenSort());
        System.out.println(producers);
    }
}
