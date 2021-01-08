package fileio;

import entities.EnergyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public final class Producer extends Observable {
    private Integer id;
    private EnergyType energyType;
    private Integer maxDistributors;
    private Float priceKW;
    private Integer energyPerDistributor;
    private List<Distributor> contractedDistributors = new ArrayList<Distributor>();
    private ArrayList<ArrayList<Integer>> mounthlyStats = new ArrayList<>();


    public Producer(final Integer id, final String energyType,
                    final Integer maxDistributors, final Float priceKW,
                    final Integer energyPerDistributor) {
        this.id = id;
        this.energyType = EnergyType.valueOf(energyType);
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
    }

    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", energyType='" + energyType + '\'' +
                ", maxDistributors=" + maxDistributors +
                ", priceKW=" + priceKW +
                ", energyPerDistributor=" + energyPerDistributor +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    public Integer getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(Integer maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public List<Distributor> getContractedDistributors() {
        return contractedDistributors;
    }

    public void setContractedDistributors(List<Distributor> contractedDistributors) {
        this.contractedDistributors = contractedDistributors;
    }

    public Float getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(Float priceKW) {
        this.priceKW = priceKW;
    }

    public Integer getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(Integer energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
        notifyObservers();
    }

    public void notifyObservers() {
        for (Distributor observer : this.contractedDistributors) {
            observer.update(this, true);
        }
        this.contractedDistributors.clear();
    }

    public ArrayList<ArrayList<Integer>> getMounthlyStats() {
        return mounthlyStats;
    }

    public void updateMonthlyStats(){
        ArrayList<Integer> stats  = new ArrayList<Integer>();
        for (Distributor distributor: this.contractedDistributors){
            stats.add(distributor.getId());
        }
        this.mounthlyStats.add(stats);
    }
}
