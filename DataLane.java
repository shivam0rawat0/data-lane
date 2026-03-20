import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import lib.consumer.Consumer;
import lib.consumer.RegularConsumer;
import lib.db.ConnectionManager;
import lib.db.StatusManager;

    /*
    Standard Pipeline:

    TB1
    seed_url(
        uid autoincrement,
        url varchar(255),
        type ENUM(HTML, JSON, XML)
    )

    P1 -> (M1) -> DATA
    key: ""
    value: "{
        "uid": 1,
        "url": "http://example.com",
        "type": ENUM(HTML, JSON, XML),
    }"

    TB2
    retry(
        jid autoincrement,
        uid int,(FK_ID)
        status ENUM(PENDING, IN_PROGRESS, FAILED, SUCCESS),
    )

    P2 -> (M2) -> RETRY
    key: ""
    value: "{
        "jid": 123,
        "url": "http://example.com",
        "type": ENUM(HTML, JSON, XML),
        "status": ENUM(PENDING, IN_PROGRESS, FAILED, SUCCESS),
    }
    */

public class DataLane {
    private static Consumer<String, String> consumer;
    private static Thread consumerThread;

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
        String url = System.getProperty("dbUrl");
        String user = System.getProperty("dbUser");
        String pass = System.getProperty("dbPass");
        ConnectionManager.setConfig(url, user, pass);
        StatusManager.setRetryCount(getRetryCount());

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
    }
}