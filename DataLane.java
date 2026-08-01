import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import lib.consumer.Consumer;
import lib.consumer.RegularConsumer;
import lib.consumer.RetryConsumer;
import lib.db.ConnectionManager;
import lib.executor.state.ExecutionGraph;

    /*
    Standard Pipeline:

    Kakfa Topic: data-pipeline
    key: ""
    value: "{
        "uid": 1,
        "data": "http://example.com",
        "type": "string",
    }"

    Context Table:
    CREATE TABLE retry (
        id INT AUTO_INCREMENT PRIMARY KEY,
        uid INT NOT NULL,
        count INT DEFAULT 0,
        status VARCHAR(50) NOT NULL,
        stage VARCHAR(50) NOT NULL,
        data varchar(1000)
    );

    Kakfa Retry Topic: data-pipeline-retry
    key: ""
    value: "{
        "jid": 123,
        "data": "http://example.com",
        "type": "string",
        "stage": "STAGE_NAME",
        "status": [PENDING, SUCCESS, RETRY, FAILED]
    }
    */

public class DataLane {
    private static Consumer<String, String> consumer;
    private static Consumer<String, String> retryConsumer;
    private static Thread consumerThread;
    private static Thread retryConsumerThread;

    private static String getTopic(){
        String topic = System.getProperty("topic");
        if(topic == null || topic.equals("")){
            topic = "data-pipeline";
        }
        return topic;
    }

    private static int getRetryCount(){
        String retryCountStr = System.getProperty("retryCount");
        if(retryCountStr == null ||retryCountStr.equals("")){
            return 2;
        }
        try{
            return Integer.parseInt(retryCountStr);
        } catch (NumberFormatException e) {
            return 2;
        }
    }

    public static void main(String[] args) {
        ExecutionGraph.setRetryCount(getRetryCount());
        ExecutionGraph.setupFlow("FETCH, FORMAT, CLASSIFY");

        String url = System.getProperty("dbUrl");
        String user = System.getProperty("dbUser");
        String pass = System.getProperty("dbPass");
        ConnectionManager.setConfig(url, user, pass);
        
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "basic-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        
        consumer = new RegularConsumer(props,List.of(getTopic()));
        consumerThread = new Thread(consumer);
        consumerThread.start();

        retryConsumer = new RetryConsumer(props, List.of("data-pipeline-retry"));
        retryConsumerThread = new Thread(retryConsumer);
        retryConsumerThread.start();
    }
}