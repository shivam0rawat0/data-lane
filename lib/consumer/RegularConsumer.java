package lib.consumer;

import java.util.List;
import java.util.Properties;
import com.alibaba.fastjson2.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import lib.entity.Job;
import lib.executor.Processor;

public class RegularConsumer extends Consumer<String, String> {
    private Processor processor = new Processor();

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
        Job job = JSON.parseObject(record.value(), Job.class);
        System.out.printf("Parsed Job: uid=%d, url=%s, type=%s%n", job.getUid(), job.getUrl(), job.getType());
        processor.submit(job);
    }
}
