package sprint_5.production;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.write.Point;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import java.time.Instant;
import java.util.List;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import sprint_5.production.SOSGame.Cell;

public class InfluxDB {
	private static final String url = "http://localhost:8086";
    private static final String token = "X3nk1vTvEhNh3B13fNWXVa0H9LKKU3W6ROX9xTfurTcBCZpE6iq4Jwvi7MxVJdHA0X7yn_xpDhCXhcsIWJqEAA==";
    private static final String org = "SOSGame";
    private static final String bucket = "SOSGameBucket";
    private InfluxDBClient client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    
    public InfluxDBClient connect() {
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }
	
	public void write(Player p, Cell move, char piece) {
		try {
            // Create a data point
            Point point = Point.measurement("temperature")
                    .addTag("location", "roomTest2")
                    .addField("test2", 0.50)
                    .time(Instant.now(), WritePrecision.NS);
            
            // Write the point
            WriteApiBlocking writeApi = client.getWriteApiBlocking();
            writeApi.writePoint(point);
            
            System.out.println("✅ Data written successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error writing data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }
	}
	
	public void query() {
		// Connect to InfluxDB
        
        try {
            // Flux query to retrieve data from the last hour
            String flux = "from(bucket: \"SOSGameBucket\")" +
                         " |> range(start: -1h)" +
                         " |> filter(fn: (r) => r._measurement == \"temperature\")";
            
            // Execute query
            List<FluxTable> tables = client.getQueryApi().query(flux);
            
            // Display results
            System.out.println("📊 Query Results:");
            for (FluxTable table : tables) {
                for (FluxRecord record : table.getRecords()) {
                    System.out.println(
                        "Time: " + record.getTime() + 
                        " | Location: " + record.getValueByKey("location") +
                        " | Value: " + record.getValue()
                    );
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error querying data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }
	}
		
}