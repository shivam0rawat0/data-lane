package lib.consumer;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class RegularConsumer extends Consumer<String, String> {

    public RegularConsumer(List<String> topics) {
        super(topics);
    }
    
    public RegularConsumer(Properties properties,List<String> topics) {
        super(properties, topics);
    }

    @Override
    public void process(ConsumerRecord<String, String> record) {
        System.out.printf("Consumed record: key=%s, value=%s, partition=%d, offset=%d%n",
                record.key(), record.value(), record.partition(), record.offset());
    }
}
