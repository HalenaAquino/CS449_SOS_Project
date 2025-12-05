package sprint_5.production;

import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.write.Point;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;


public class InfluxDB {
	private static final String url = "http://localhost:8086";
    private static final String token = "X3nk1vTvEhNh3B13fNWXVa0H9LKKU3W6ROX9xTfurTcBCZpE6iq4Jwvi7MxVJdHA0X7yn_xpDhCXhcsIWJqEAA==";
    private static final String org = "SOSGame";
    private static final String bucket = "SOSGameBucket";
    private static InfluxDBClient client = null;
    private static int moveID = 0;
    
    public static InfluxDBClient connect() {
    	if (client == null) {
            client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        }
        return client;
    }
	
	public static void write(String player, String piece, int xCoord, int yCoord) {
		moveID++;
		//System.out.println(moveID);
		// create the row
		Point point = Point.measurement("SOSGameRecord")
				.addField("ID", moveID)
				.addField("Player", player)
				.addField("Piece", piece)
				.addField("x_coordinate", xCoord)
				.addField("y_coordinate", yCoord)
				.time(Instant.now(), WritePrecision.NS);
		
		// Write the point
		WriteApiBlocking writeApi = connect().getWriteApiBlocking();
		writeApi.writePoint(point);            
	}
	
	public static List<List<Object>> query() {
		//TODO
		
		//List[] rows;

		// pulls out the specific data needed
		String flux = "from(bucket: \"SOSGameBucket\")" +
	              " |> range(start: -24h)" +
	              " |> filter(fn: (r) => r._measurement == \"SOSGameRecord\")" +
	              " |> sort(columns: [\"ID\"], desc: false)" +
	              " |> pivot(rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";

		
        List<FluxTable> tables = client.getQueryApi().query(flux);
        List<List<Object>> rows = new ArrayList<>();
        
        for (FluxTable table : tables) {
        	for (FluxRecord record : table.getRecords()) {
        		List<Object> row = new ArrayList<>();
        		// extracts each value
        		//Object id = record.getValueByKey("id")
        		String player = (String) record.getValueByKey("Player");
        		String piece = (String) record.getValueByKey("Piece");
        		Object xCoord = record.getValueByKey("x_coordinate");
        		Object yCoord = record.getValueByKey("y_coordinate");
            		
        		row.add(player);
        		row.add(piece);
        		row.add(xCoord);
        		row.add(yCoord);
            		            		
        		rows.add(row);
        	}
        }
        
        return rows;
	}
	
	public static void clearTable() {
        DeleteApi deleteApi = client.getDeleteApi();

        OffsetDateTime start = Instant.parse("2025-12-01T00:00:00Z").atOffset(ZoneOffset.UTC);
        OffsetDateTime stop  = Instant.now().atOffset(ZoneOffset.UTC);

        String predicate = "_measurement=\"SOSGameRecord\"";		// deletes the recorded table

        deleteApi.delete(start, stop, predicate, bucket, org);
	}
	
	
	
	
	// used to test this class' functions
	public static void main(String[] args) {
		write("Blue", "S", 0, 0);
		write("Red", "O", 2, 4);
		write("Blue", "O", 1, 1);
		write("Red", "S", 2, 1);
		
		System.out.println(query());
		clearTable();
		
		
		client.close();
		client = null;
		
		System.out.println("completed");
	}
		
}