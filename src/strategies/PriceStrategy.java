package strategies;

import fileio.Distributor;
import fileio.Producer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class PriceStrategy implements Strategy {

    static class PriceSort implements Comparator<Producer> {
        @Override
        public int compare(final Producer o1, final Producer o2) {
            if (o1.getPriceKW().compareTo(o2.getPriceKW()) == 0) {
                return o2.getEnergyPerDistributor().compareTo(o1.getEnergyPerDistributor());
            }
            return o1.getPriceKW().compareTo(o2.getPriceKW());
        }
    }

    @Override
    public void applyStrategy(Distributor distributor, List<Producer> producers) {
        for (Producer producer : producers) {
            producer.getContractedDistributors().remove(distributor);
        }
        distributor.getContractedProducers().clear();
        List<Producer> producersCopy = new ArrayList<Producer>(producers);
        producersCopy.sort(new PriceSort());
        Integer energyNeededKw = distributor.getEnergyNeededKW();
        while (energyNeededKw > 0) {
            if (producersCopy.get(0).getContractedDistributors().size()
                    == producersCopy.get(0).getMaxDistributors()) {
                producersCopy.remove(0);
                continue;
            }
            distributor.getContractedProducers().add(producersCopy.get(0));
            producers.get(producers.indexOf(producersCopy.get(0))).
                    getContractedDistributors().add(distributor);
            energyNeededKw -= producersCopy.get(0).getEnergyPerDistributor();
            producersCopy.remove(0);
        }
        distributor.setNeedUpdate(false);
        distributor.setProductionCost();
    }
}
