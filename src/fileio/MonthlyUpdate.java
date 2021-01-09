package fileio;

import java.util.List;

public final class MonthlyUpdate {
    private List<Consumer> newConsumers;
    private List<DistributorChanges> distributorChanges;
    private List<ProducerChanges> producerChanges;

    public MonthlyUpdate(final List<Consumer> newConsumers,
                         final List<DistributorChanges> distributorChanges,
                         final List<ProducerChanges> producerChanges) {
        this.newConsumers = newConsumers;
        this.distributorChanges = distributorChanges;
        this.producerChanges = producerChanges;
    }

    public List<Consumer> getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final List<Consumer> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public List<DistributorChanges> getDistributorChanges() {
        return distributorChanges;
    }

    public void setDistributorChanges(final List<DistributorChanges> distributorChanges) {
        this.distributorChanges = distributorChanges;
    }

    public List<ProducerChanges> getProducerChanges() {
        return producerChanges;
    }

    public void setProducerChanges(final List<ProducerChanges> producerChanges) {
        this.producerChanges = producerChanges;
    }

}
