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
    private static InfluxDBClient client = null;
    
    public static InfluxDBClient connect() {
    	if (client == null) {
            client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        }
        return client;
    }
	
	public static void write(String player, String piece, int xCoord, int yCoord) {
		// create the row
		Point point = Point.measurement("SOSGameRecord")
				.addField("Player", player)
				.addField("Piece", piece)
				.addField("x_coordinate", xCoord)
				.addField("y_coordinate", yCoord)
				.time(Instant.now(), WritePrecision.NS);
		
		// Write the point
		WriteApiBlocking writeApi = connect().getWriteApiBlocking();
		writeApi.writePoint(point);            
	}
	
	public void query() {
		//TODO
	}
	
	public void clearTable() {
		//TODO
	}
	
	
	
	
	// used to test write
	public static void main(String[] args) {
		write("Blue", "S", 0, 0);
		write("Red", "O", 2, 4);
		write("Blue", "O", 1, 1);
		write("Red", "S", 2, 1);
		
		client.close();
		client = null;
		
		System.out.println("rows added");
	}
		
}