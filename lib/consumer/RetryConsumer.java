package lib.consumer;

import java.util.List;
import java.util.Properties;
import com.alibaba.fastjson2.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import lib.db.Retry;
import lib.executor.Processor;

public class RetryConsumer extends Consumer<String, String> {
    private Processor processor = new Processor();

    public RetryConsumer(List<String> topics) {
        super(topics);
    }
    
    public RetryConsumer(Properties properties,List<String> topics) {
        super(properties, topics);
    }

    @Override
    public void process(ConsumerRecord<String, String> record) {
        System.out.printf("Consumed record: key=%s, value=%s, partition=%d, offset=%d%n",
                record.key(), record.value(), record.partition(), record.offset());
        Retry retry = JSON.parseObject(record.value(), Retry.class);
        System.out.printf("Parsed Retry: id=%d, uid=%d, data=%s%n", retry.getId(), retry.getUid(), retry.getData());
        processor.submitRetry(retry);
    }
}
