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
     *
     * @return the input data
     */
    public Input readData() {
        JSONParser jsonParser = new JSONParser();
        Integer numberofTurns = 0;
        List<Consumer> consumersData = new ArrayList<Consumer>();
        List<Distributor> distributorsData = new ArrayList<Distributor>();
        List<MonthlyUpdate> monthlyUpdatesData = new ArrayList<MonthlyUpdate>();

        ConsumerFactory consumerFactory = ConsumerFactory.getInstance();
        DistributorFactory distributorFactory = DistributorFactory.getInstance();

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
                                Integer.parseInt(String.valueOf(d.get("initialProductionCost")))));
            }

            JSONArray updates = (JSONArray) jsonObject.get("monthlyUpdates");
            for (Object data : updates) {
                List<Consumer> newConsumers = new ArrayList<Consumer>();
                List<CostChanges> costChanges = new ArrayList<CostChanges>();
                JSONArray newcons = (JSONArray) ((JSONObject) data).get("newConsumers");
                for (Object newconsumers : newcons) {
                    JSONObject c = (JSONObject) newconsumers;
                    newConsumers.add(consumerFactory.
                            createConsumer(Integer.parseInt(String.valueOf(c.get("id"))),
                                    Integer.parseInt(String.valueOf(c.get("initialBudget"))),
                                    Integer.parseInt(String.valueOf(c.get("monthlyIncome")))));
                }
                JSONArray cchanges = (JSONArray) ((JSONObject) data).get("costsChanges");
                for (Object changes : cchanges) {
                    JSONObject c = (JSONObject) changes;
                    CostChanges change = new CostChanges(Integer.
                            parseInt(String.valueOf(c.get("id"))),
                            Integer.parseInt(String.valueOf(c.get("infrastructureCost"))),
                            Integer.parseInt(String.valueOf(c.get("productionCost"))));
                    costChanges.add(change);
                }
                monthlyUpdatesData.add(new MonthlyUpdate(newConsumers, costChanges));
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return new Input(numberofTurns, consumersData, distributorsData, monthlyUpdatesData);
    }
}
