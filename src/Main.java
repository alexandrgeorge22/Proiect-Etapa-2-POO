import fileio.*;
import strategies.StrategyFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    /**
     * @param input data to apply the initial round
     */
    public static void initialround(Input input) {
        StrategyFactory strategyFactory = new StrategyFactory();
        // Runda initiala
        // Calculare pret contracte
        // aplicare strategii + calculare cost productie
        for (Distributor distributor : input.getDistributorsData()) {
            strategyFactory.createStrategy(distributor.getEnergyChoiceStrategyType()).
                    applyStrategy(distributor, input.getProducersData());
            distributor.setProductionCost();
            distributor.setContractPrice(0);
        }
        // Runda initiala
        // Calculare pret contracte

        List<Distributor> distributors = new ArrayList<>(input.getDistributorsData());
        distributors.sort(Comparator.comparing(Distributor::getContractPrice));
        // Alegere contracte, primire salariu, plata rata
        for (Consumer consumer : input.getConsumersData()) {
            consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());
            consumer.setDistributor(distributors.get(0).getId());
            consumer.setActualContractLength(distributors.get(0).getContractLength());
            consumer.setContractPrice(distributors.get(0).getContractPrice());
            input.getDistributorsData().get(consumer.getDistributor()).setContractedConsumers(input.
                    getDistributorsData().get(consumer.getDistributor()).
                    getContractedConsumers() + 1);
            if (consumer.getBudget() >= consumer.getContractPrice()) {
                consumer.setBudget(consumer.getBudget() - consumer.getContractPrice());
                input.getDistributorsData().get(consumer.getDistributor()).setBudget(input.
                        getDistributorsData().get(consumer.getDistributor()).getBudget()
                        + consumer.getContractPrice());
                consumer.setActualContractLength(consumer.getActualContractLength() - 1);
            } else if (consumer.getPenalty() == null) {
                consumer.setPenalty(consumer.getContractPrice());
                consumer.setActualContractLength(consumer.getActualContractLength() - 1);
            } else {
                consumer.setBankrupt(true);
            }
        }
        // Plata cheltuielilor
        for (Distributor distributor : input.getDistributorsData()) {
            int totalcost = distributor.getInfrastructureCost()
                    + distributor.getProductionCost() * distributor.getContractedConsumers();
            distributor.setBudget(distributor.getBudget() - totalcost);
            if (distributor.getBudget() <= 0) {
                distributor.setBankrupt(true);
                for (Consumer consumer : input.getConsumersData()) {
                    if (consumer.getDistributor().equals(distributor.getId())) {
                        consumer.setDistributor(null);
                        consumer.setPenalty(null);
                        consumer.setContractPrice(null);
                        consumer.setActualContractLength(null);
                    }
                }
            }
        }
    }

    public static void main(final String[] args) throws Exception {
        InputLoader inputLoader = new InputLoader(args[0]);
        Input input = inputLoader.readData();


        initialround(input);


        // Rundele
        for (int i = 0; i < input.getNumberofTurns(); i++) {

            // Citire update-uri
            input.getConsumersData().addAll(input.getMonthlyUpdatesData().get(i).getNewConsumers());
            for (DistributorChanges distributorChange : input.getMonthlyUpdatesData().get(i).getDistributorChanges()) {
                input.getDistributorsData().get(distributorChange.getId()).
                        setInfrastructureCost(distributorChange.getInfrastructureCost());
            }

            // Calculare pret nou pentru contracte
            for (Distributor distributor : input.getDistributorsData()) {
                if (distributor.isBankrupt()) {
                    distributor.setContractPrice(null);
                    for (Consumer consumer : input.getConsumersData()) {
                        if (consumer.getDistributor() != null
                                && consumer.getDistributor().equals(distributor.getId())) {
                            consumer.setDistributor(null);
                        }
                    }
                } else {
                    distributor.setContractPrice(distributor.getContractedConsumers());
                }
            }

            // Sortare crescatoare dupa pretul contractului
            List<Distributor> updateddistributors = new ArrayList<>(input.getDistributorsData());
            updateddistributors.sort(Comparator.comparing(Distributor::getContractPrice));

            // Primire salariu consumatori, alegere contract, plata rata
            for (Consumer consumer : input.getConsumersData()) {
                if (!consumer.isBankrupt()) {
                    // primire salariu
                    consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());
                    // verificare daca se termina contractul si daca are penalitati
                    // sa le plateasca ca sa o poata lua de la inceput cu un nou contract
                    if (consumer.getActualContractLength() == 0) {
                        consumer.setActualContractLength(updateddistributors.get(0).
                                getContractLength());
                        consumer.setContractPrice(updateddistributors.get(0).
                                getContractPrice());
                        if (consumer.getPenalty() != null) {
                            Integer penalty = Math.toIntExact(Math.round(Math.floor(1.2 * consumer.
                                    getPenalty())));
                            if (consumer.getBudget() >= penalty + consumer.getContractPrice()) {
                                consumer.setBudget(consumer.getBudget() - penalty - consumer.
                                        getContractPrice());
                                input.getDistributorsData().get(consumer.getDistributor()).
                                        setBudget(input.getDistributorsData().get(consumer.
                                                getDistributor()).getBudget() + penalty);
                                input.getDistributorsData().get(consumer.getDistributor()).
                                        setContractedConsumers(input.getDistributorsData().
                                                get(consumer.getDistributor()).
                                                getContractedConsumers() - 1);
                                consumer.setPenalty(null);
                                consumer.setDistributor(updateddistributors.get(0).getId());
                                input.getDistributorsData().get(consumer.getDistributor()).
                                        setContractedConsumers(input.getDistributorsData().
                                                get(consumer.getDistributor()).
                                                getContractedConsumers() + 1);
                            } else {
                                consumer.setBankrupt(true);
                            }
                        } else {
                            if (consumer.getDistributor() != null) {
                                input.getDistributorsData().get(consumer.getDistributor()).
                                        setContractedConsumers(input.getDistributorsData().
                                                get(consumer.getDistributor()).
                                                getContractedConsumers() - 1);
                            }
                            consumer.setDistributor(updateddistributors.get(0).getId());
                            input.getDistributorsData().get(consumer.getDistributor()).
                                    setContractedConsumers(input.
                                            getDistributorsData().get(consumer.getDistributor()).
                                            getContractedConsumers() + 1);
                        }
                    }
                    // alegere contract nou
                    if (consumer.getDistributor() == null) {
                        consumer.setDistributor(updateddistributors.get(0).getId());
                        consumer.setActualContractLength(updateddistributors.get(0).
                                getContractLength());
                        consumer.setContractPrice(updateddistributors.get(0).
                                getContractPrice());
                        input.getDistributorsData().get(consumer.getDistributor()).
                                setContractedConsumers(input.
                                        getDistributorsData().get(consumer.getDistributor()).
                                        getContractedConsumers() + 1);
                    }
                    // plata rata
                    if (consumer.getDistributor() != null) {
                        if (consumer.getBudget() >= consumer.getContractPrice()) {
                            if (consumer.getPenalty() != null) {
                                Integer penalty = Math.toIntExact(Math.round(Math.
                                        floor(1.2 * consumer.getPenalty())));
                                if (consumer.getBudget() >= consumer.getContractPrice() + penalty) {
                                    consumer.setBudget(consumer.getBudget() - consumer.
                                            getContractPrice() - penalty);
                                    consumer.setPenalty(null);
                                    input.getDistributorsData().get(consumer.
                                            getDistributor()).setBudget(input.
                                            getDistributorsData().get(consumer.
                                            getDistributor()).getBudget()
                                            + consumer.getContractPrice() + penalty);
                                    consumer.setActualContractLength(consumer.
                                            getActualContractLength() - 1);
                                } else {
                                    consumer.setBankrupt(true);
                                }
                            } else {
                                consumer.setBudget(consumer.getBudget() - consumer.
                                        getContractPrice());
                                input.getDistributorsData().get(consumer.
                                        getDistributor()).setBudget(input.getDistributorsData().
                                        get(consumer.getDistributor()).getBudget()
                                        + consumer.getContractPrice());
                                consumer.setActualContractLength(consumer.
                                        getActualContractLength() - 1);
                            }
                        } else if (consumer.getPenalty() == null) {
                            consumer.setPenalty(consumer.getContractPrice());
                            consumer.setActualContractLength(consumer.
                                    getActualContractLength() - 1);
                        } else {
                            consumer.setBankrupt(true);
                        }
                    }
                }
            }
            // Plata cheltuielilor
            for (Distributor distributor : input.getDistributorsData()) {
                if (!distributor.isBankrupt()) {
                    int totalcost = distributor.getInfrastructureCost() + distributor.
                            getProductionCost() * distributor.getContractedConsumers();
                    if (distributor.getBudget() < totalcost) {
                        distributor.setBudget(distributor.getBudget() - distributor.
                                getInfrastructureCost());
                        distributor.setBankrupt(true);
                        for (Producer producer : input.getProducersData()) {
                            if (distributor.getContractedProducers().contains(producer)) {
                                producer.getContractedDistributors().remove(distributor);
                            }
                        }
                        distributor.getContractedProducers().clear();
                        for (Consumer consumer : input.getConsumersData()) {
                            if (consumer.getDistributor() != null
                                    && consumer.getDistributor().equals(distributor.getId())) {
                                consumer.setDistributor(null);
                                consumer.setPenalty(null);
                                consumer.setContractPrice(null);
                                consumer.setActualContractLength(null);
                            }
                        }
                    } else {
                        distributor.setBudget(distributor.getBudget() - totalcost);
                    }
                    for (Consumer consumer : input.getConsumersData()) {
                        if (consumer.isBankrupt()) {
                            if (consumer.getDistributor() != null
                                    && consumer.getDistributor().equals(distributor.getId())) {
                                distributor.setContractedConsumers(distributor.
                                        getContractedConsumers() - 1);
                                consumer.setDistributor(null);
                            }
                        }
                    }
                }
            }

            for (ProducerChanges producerChange : input.getMonthlyUpdatesData().get(i).getProducerChanges()) {
                input.getProducersData().get(producerChange.getId()).
                        setEnergyPerDistributor(producerChange.getEnergyPerDistributor());
            }
            StrategyFactory strategyFactory = new StrategyFactory();
            for (Distributor distributor : input.getDistributorsData()) {
                if (!distributor.isBankrupt() && distributor.NeedUpdate()) {
                    strategyFactory.createStrategy(distributor.getEnergyChoiceStrategyType()).
                            applyStrategy(distributor, input.getProducersData());
                    distributor.setProductionCost();
                    if (input.getNumberofTurns().compareTo(i + 1) != 0) {
                        distributor.setContractPrice(distributor.getContractedConsumers());
                    }

                }
            }
            for (Producer producer : input.getProducersData()) {
                producer.updateMonthlyStats();
            }
        }

        FileWriter writer = new FileWriter(args[1], input);
        writer.writefile();


    }
}
