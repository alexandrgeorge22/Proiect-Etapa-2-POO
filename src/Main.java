import fileio.Consumer;
import fileio.Distributor;
import fileio.Producer;
import fileio.Input;
import fileio.InputLoader;
import fileio.DistributorChanges;
import fileio.ProducerChanges;
import fileio.FileWriter;
import strategies.StrategyFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    /**
     * @param input data to apply the initial round
     */
    public static void initialround(Input input) {
        StrategyFactory strategyFactory = StrategyFactory.getInstance();
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
            consumer.setDistributor(distributors.get(0));
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
        StrategyFactory strategyFactory = StrategyFactory.getInstance();
        initialround(input);

        // Rundele
        for (int i = 0; i < input.getNumberofTurns(); i++) {
            // Citire update-uri
            input.getConsumersData().addAll(input.getMonthlyUpdatesData().get(i).getNewConsumers());
            for (DistributorChanges distributorChange : input.getMonthlyUpdatesData().
                    get(i).getDistributorChanges()) {
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
                    consumer.checkContract(input.getDistributorsData(), updateddistributors);

                    // alegere contract nou
                    if (consumer.getDistributor() == null) {
                        consumer.setDistributor(updateddistributors.get(0));
                        input.getDistributorsData().get(consumer.getDistributor()).
                                setContractedConsumers(input.
                                        getDistributorsData().get(consumer.getDistributor()).
                                        getContractedConsumers() + 1);
                    }
                    // plata rata
                    if (consumer.getDistributor() != null) {
                        consumer.payContract(input.getDistributorsData());
                    }
                }
            }
            // Plata cheltuielilor
            for (Distributor distributor : input.getDistributorsData()) {
                if (!distributor.isBankrupt()) {
                    distributor.payCosts(input.getConsumersData(), input.getProducersData());
                }
            }
            // Actualizare producatori
            for (ProducerChanges producerChange : input.getMonthlyUpdatesData().
                    get(i).getProducerChanges()) {
                input.getProducersData().get(producerChange.getId()).
                        setEnergyPerDistributor(producerChange.getEnergyPerDistributor());
            }
            // Reaplicare strategie producatori
            for (Distributor distributor : input.getDistributorsData()) {
                if (!distributor.isBankrupt() && distributor.needUpdate()) {
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
