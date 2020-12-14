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
    private String outFile;
    private ObjectMapper mapper = new ObjectMapper();
    private ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());


    public FileWriter(final String outFile, final Input output) {
        this.output = output;
        this.outFile = outFile;
    }

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
            d.put("budget", distributor.getBudget());
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
        writer.writeValue(Paths.get(outFile).toFile(), out);
    }

}
