package fileio;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class InputLoader {

    private final String inputPath;

    /**
     * @param inputPath set the path of the input file
     */
    public InputLoader(final String inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * @return the input data
     */
    public Input readData() {
        JSONParser jsonParser = new JSONParser();
        Integer numberofTurns = 0;
        List<Consumer> consumersData = new ArrayList<Consumer>();
        List<Distributor> distributorsData = new ArrayList<Distributor>();
        List<Producer> producersData = new ArrayList<Producer>();
        List<MonthlyUpdate> monthlyUpdatesData = new ArrayList<MonthlyUpdate>();

        ConsumerFactory consumerFactory = ConsumerFactory.getInstance();
        DistributorFactory distributorFactory = DistributorFactory.getInstance();
        ProducerFactory producerFactory = ProducerFactory.getInstance();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser
                    .parse(new FileReader(inputPath));

            numberofTurns = Integer.parseInt(jsonObject.get("numberOfTurns").toString());

            JSONObject initialData = (JSONObject) jsonObject.get("initialData");
            JSONArray consumers = (JSONArray) initialData.get("consumers");
            for (Object data : consumers) {
                JSONObject c = (JSONObject) data;
                consumersData.add(consumerFactory.
                        createConsumer(Integer.parseInt(String.valueOf(c.get("id"))),
                                Integer.parseInt(String.valueOf(c.get("initialBudget"))),
                                Integer.parseInt(String.valueOf(c.get("monthlyIncome")))));
            }

            JSONArray distributors = (JSONArray) initialData.get("distributors");
            for (Object data : distributors) {
                JSONObject d = (JSONObject) data;
                distributorsData.add(distributorFactory.
                        createDistributor(Integer.parseInt(String.valueOf(d.get("id"))),
                                Integer.parseInt(String.valueOf(d.get("contractLength"))),
                                Integer.parseInt(String.valueOf(d.get("initialBudget"))),
                                Integer.parseInt(String.valueOf(d.get("initialInfrastructureCost"))),
                                Integer.parseInt(String.valueOf(d.get("energyNeededKW"))),
                                String.valueOf(d.get("producerStrategy"))));
            }

            JSONArray producers = (JSONArray) initialData.get("producers");
            for (Object data : producers) {
                JSONObject p = (JSONObject) data;
                producersData.add(producerFactory.
                        createProducer(Integer.parseInt(String.valueOf(p.get("id"))),
                                String.valueOf(p.get("energyType")),
                                Integer.parseInt(String.valueOf(p.get("maxDistributors"))),
                                Float.parseFloat(String.valueOf(p.get("priceKW"))),
                                Integer.parseInt(String.valueOf(p.get("energyPerDistributor")))));
            }

            JSONArray updates = (JSONArray) jsonObject.get("monthlyUpdates");
            for (Object data : updates) {
                List<Consumer> newConsumers = new ArrayList<Consumer>();
                List<DistributorChanges> distributorChanges = new ArrayList<DistributorChanges>();
                List<ProducerChanges> producerChanges = new ArrayList<ProducerChanges>();
                JSONArray newcons = (JSONArray) ((JSONObject) data).get("newConsumers");
                for (Object newconsumers : newcons) {
                    JSONObject c = (JSONObject) newconsumers;
                    newConsumers.add(consumerFactory.
                            createConsumer(Integer.parseInt(String.valueOf(c.get("id"))),
                                    Integer.parseInt(String.valueOf(c.get("initialBudget"))),
                                    Integer.parseInt(String.valueOf(c.get("monthlyIncome")))));
                }
                JSONArray distChanges = (JSONArray) ((JSONObject) data).get("distributorChanges");
                for (Object changes : distChanges) {
                    JSONObject c = (JSONObject) changes;
                    distributorChanges.add(new DistributorChanges(Integer.
                            parseInt(String.valueOf(c.get("id"))),
                            Integer.parseInt(String.valueOf(c.get("infrastructureCost")))));
                }
                JSONArray prodChanges = (JSONArray) ((JSONObject) data).get("producerChanges");
                for (Object changes : prodChanges) {
                    JSONObject c = (JSONObject) changes;
                    producerChanges.add(new ProducerChanges(Integer.
                            parseInt(String.valueOf(c.get("id"))),
                            Integer.parseInt(String.valueOf(c.get("energyPerDistributor")))));
                }

                monthlyUpdatesData.add(new MonthlyUpdate(newConsumers, distributorChanges, producerChanges));
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return new Input(numberofTurns, consumersData, distributorsData, producersData, monthlyUpdatesData);
    }
}
