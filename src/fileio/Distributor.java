package fileio;

import strategies.EnergyChoiceStrategyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public final class Distributor implements Observer {
    private Integer id;
    private Integer contractLength;
    private Integer budget;
    private Integer infrastructureCost;
    private Integer productionCost;
    private Integer contractPrice;
    private Integer contractedConsumers = 0;
    private boolean isBankrupt = false;
    private Integer energyNeededKW;
    private EnergyChoiceStrategyType energyChoiceStrategyType;
    private List<Producer> contractedProducers = new ArrayList<Producer>();
    private boolean needUpdate = false;

    /**
     * @return if the distributor need to update the contracted producers
     */
    public boolean needUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public Distributor(final Integer id, final Integer contractLength,
                       final Integer budget, final Integer infrastructureCost,
                       final Integer energyNeededKW, final String producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.budget = budget;
        this.infrastructureCost = infrastructureCost;
        this.energyNeededKW = energyNeededKW;
        this.energyChoiceStrategyType = EnergyChoiceStrategyType.valueOf(producerStrategy);
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;

    }

    public Integer getContractedConsumers() {
        return contractedConsumers;
    }

    public void setContractedConsumers(final Integer contractedConsumers) {
        this.contractedConsumers = contractedConsumers;
    }

    public Integer getContractPrice() {
        return contractPrice;
    }

    /**
     * @param contractedConsumers of distributor used to calculate the contract price
     */
    public void setContractPrice(Integer contractedConsumers) {
        int infinity = 999999999;
        if (contractedConsumers == null) {
            this.contractPrice = infinity;
        } else {
            if (contractedConsumers.equals(0)) {
                contractedConsumers = 1;
            }
            Integer profit = Math.toIntExact(Math.round(Math.floor(0.2 * this.productionCost)));
            this.contractPrice = Math.toIntExact(Math.round(Math.floor(
                    this.infrastructureCost / contractedConsumers) + this.productionCost + profit));
        }

    }

    /**
     * calculate the production cost
     */
    public void setProductionCost() {
        float cost = 0;
        for (Producer producer : this.contractedProducers) {
            cost += producer.getPriceKW() * producer.getEnergyPerDistributor();
        }
        this.productionCost = Math.toIntExact(Math.round(Math.floor(cost / 10)));
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(final Integer budget) {
        this.budget = budget;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getContractLength() {
        return contractLength;
    }

    public void setContractLength(final Integer contractLength) {
        this.contractLength = contractLength;
    }

    public Integer getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final Integer infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public Integer getProductionCost() {
        return productionCost;
    }

    public List<Producer> getContractedProducers() {
        return contractedProducers;
    }

    public void setContractedProducers(List<Producer> contractedProducers) {
        this.contractedProducers = contractedProducers;

    }

    public Integer getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(Integer energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public EnergyChoiceStrategyType getEnergyChoiceStrategyType() {
        return energyChoiceStrategyType;
    }

    public void setEnergyChoiceStrategyType(EnergyChoiceStrategyType energyChoiceStrategyType) {
        this.energyChoiceStrategyType = energyChoiceStrategyType;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.needUpdate = (boolean) arg;
    }

    public void payCosts(List<Consumer> consumersData, List<Producer> producersData) {
        int totalcost = this.getInfrastructureCost() + this.
                getProductionCost() * this.getContractedConsumers();
        if (this.getBudget() < totalcost) {
            this.setBudget(this.getBudget() - this.
                    getInfrastructureCost());
            this.setBankrupt(true);
            for (Producer producer : producersData) {
                producer.getContractedDistributors().remove(this);
            }
            this.getContractedProducers().clear();
            for (Consumer consumer : consumersData) {
                if (consumer.getDistributor() != null
                        && consumer.getDistributor().equals(this.getId())) {
                    consumer.setDistributor(null);
                    consumer.setPenalty(null);
                    consumer.setContractPrice(null);
                    consumer.setActualContractLength(null);
                }
            }
        } else {
            this.setBudget(this.getBudget() - totalcost);
        }
        for (Consumer consumer : consumersData) {
            if (consumer.isBankrupt()) {
                if (consumer.getDistributor() != null
                        && consumer.getDistributor().equals(this.getId())) {
                    this.setContractedConsumers(this.
                            getContractedConsumers() - 1);
                    consumer.setDistributor(null);
                }
            }
        }
    }
}
