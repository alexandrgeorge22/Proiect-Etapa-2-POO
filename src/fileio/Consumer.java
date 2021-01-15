package fileio;

import java.util.List;

public final class Consumer {
    private Integer id;
    private Integer budget;
    private Integer monthlyIncome;
    private Integer penalty = null;
    private Integer actualContractLength = 0;
    private Integer distributor = null;
    private Integer contractPrice = null;
    private boolean isBankrupt = false;


    public Consumer(final Integer id, final Integer initialBudget,
                    final Integer monthlyIncome) {
        this.id = id;
        this.budget = initialBudget;
        this.monthlyIncome = monthlyIncome;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public Integer getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(final Integer contractPrice) {
        this.contractPrice = contractPrice;
    }

    public void setPenalty(final Integer penalty) {
        this.penalty = penalty;
    }

    public Integer getActualContractLength() {
        return actualContractLength;
    }

    public void setActualContractLength(final Integer actualContractLength) {
        this.actualContractLength = actualContractLength;
    }

    public Integer getDistributor() {
        return distributor;
    }

    public void setDistributor(final Distributor distributor) {

        if (distributor == null) {
            this.distributor = null;
        } else {
            this.setActualContractLength(distributor.getContractLength());
            this.setContractPrice(distributor.getContractPrice());
            this.distributor = distributor.getId();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(final Integer budget) {
        this.budget = budget;
    }

    public Integer getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(final Integer monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public void checkContract(final List<Distributor> distributorsData,
                              final List<Distributor> updateddistributors) {
        if (this.getActualContractLength() == 0) {
            if (this.getPenalty() != null) {
                Integer penalty = Math.toIntExact(Math.round(Math.floor(1.2 * this.
                        getPenalty())));
                if (this.getBudget() >= penalty + this.getContractPrice()) {
                    this.setBudget(this.getBudget() - penalty - this.
                            getContractPrice());
                    distributorsData.get(this.getDistributor()).
                            setBudget(distributorsData.get(this.
                                    getDistributor()).getBudget() + penalty);
                    distributorsData.get(this.getDistributor()).
                            setContractedConsumers(distributorsData.
                                    get(this.getDistributor()).
                                    getContractedConsumers() - 1);
                    this.setPenalty(null);
                    this.setDistributor(updateddistributors.get(0));
                    distributorsData.get(this.getDistributor()).
                            setContractedConsumers(distributorsData.
                                    get(this.getDistributor()).
                                    getContractedConsumers() + 1);
                } else {
                    this.setBankrupt(true);
                }
            } else {
                if (this.getDistributor() != null) {
                    distributorsData.get(this.getDistributor()).
                            setContractedConsumers(distributorsData.
                                    get(this.getDistributor()).
                                    getContractedConsumers() - 1);
                }
                this.setDistributor(updateddistributors.get(0));
                distributorsData.get(this.getDistributor()).
                        setContractedConsumers(distributorsData.get(this.getDistributor()).
                                getContractedConsumers() + 1);
            }
        }
    }

    public void payContract(List<Distributor> distributorsData) {
        if (this.getBudget() >= this.getContractPrice()) {
            if (this.getPenalty() != null) {
                Integer penalty = Math.toIntExact(Math.round(Math.
                        floor(1.2 * this.getPenalty())));
                if (this.getBudget() >= this.getContractPrice() + penalty) {
                    this.setBudget(this.getBudget() - this.
                            getContractPrice() - penalty);
                    this.setPenalty(null);
                    distributorsData.get(this.
                            getDistributor()).setBudget(
                            distributorsData.get(this.
                                    getDistributor()).getBudget()
                                    + this.getContractPrice() + penalty);
                    this.setActualContractLength(this.
                            getActualContractLength() - 1);
                } else {
                    this.setBankrupt(true);
                }
            } else {
                this.setBudget(this.getBudget() - this.
                        getContractPrice());
                distributorsData.get(this.
                        getDistributor()).setBudget(distributorsData.
                        get(this.getDistributor()).getBudget()
                        + this.getContractPrice());
                this.setActualContractLength(this.
                        getActualContractLength() - 1);
            }
        } else if (this.getPenalty() == null) {
            this.setPenalty(this.getContractPrice());
            this.setActualContractLength(this.
                    getActualContractLength() - 1);
        } else {
            this.setBankrupt(true);
        }
    }
}
