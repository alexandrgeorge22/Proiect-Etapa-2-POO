package fileio;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

public final class FileWriter {

    private final Input output;
    private final String outFile;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());


    public FileWriter(final String outFile, final Input output) {
        this.output = output;
        this.outFile = outFile;
    }

    /**
     * Write the rounds result to output file in json format
     */
    public void writefile() throws IOException {

        JSONObject out = new JSONObject();
        JSONArray consumers = new JSONArray();
        for (Consumer consumer : output.getConsumersData()) {
            JSONObject c = new JSONObject();
            c.put("id", consumer.getId());
            c.put("isBankrupt", consumer.isBankrupt());
            c.put("budget", consumer.getBudget());
            consumers.add(c);
        }
        out.put("consumers", consumers);
        JSONArray distributors = new JSONArray();
        for (Distributor distributor : output.getDistributorsData()) {
            Collections.sort(output.getConsumersData(), ((o1, o2) -> o1.getActualContractLength().
                    compareTo(o2.getActualContractLength())));
            JSONObject d = new JSONObject();
            d.put("id", distributor.getId());
            d.put("energyNeededKW", distributor.getEnergyNeededKW());
            d.put("contractCost", distributor.getContractPrice());
            d.put("budget", distributor.getBudget());
            d.put("producerStrategy", distributor.getEnergyChoiceStrategyType().getLabel());
            d.put("isBankrupt", distributor.isBankrupt());
            JSONArray cons = new JSONArray();
            for (Consumer consumer : output.getConsumersData()) {
                if (consumer.getDistributor() != null
                        && consumer.getDistributor().equals(distributor.getId())) {
                    JSONObject c = new JSONObject();
                    c.put("consumerId", consumer.getId());
                    c.put("price", consumer.getContractPrice());
                    c.put("remainedContractMonths", consumer.getActualContractLength());
                    cons.add(c);
                }
            }
            d.put("contracts", cons);
            distributors.add(d);
        }
        out.put("distributors", distributors);
        JSONArray producers = new JSONArray();
        for (Producer producer : output.getProducersData()) {
            JSONObject p = new JSONObject();
            p.put("id", producer.getId());
            p.put("maxDistributors", producer.getMaxDistributors());
            p.put("priceKW", producer.getPriceKW());
            p.put("energyType", producer.getEnergyType().getLabel());
            p.put("energyPerDistributor", producer.getEnergyPerDistributor());
            JSONArray monthlyStats = new JSONArray();
            for (int i = 0; i < producer.getMounthlyStats().size(); i++) {
                JSONObject month = new JSONObject();
                month.put("month", i + 1);
                month.put("distributorsIds", producer.getMounthlyStats().get(i));
                monthlyStats.add(month);
            }
            p.put("monthlyStats", monthlyStats);
            producers.add(p);
        }
        out.put("energyProducers", producers);
        writer.writeValue(Paths.get(outFile).toFile(), out);
    }

}
