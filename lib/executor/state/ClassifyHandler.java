package lib.executor.state;

import java.util.Arrays;
import java.util.stream.Collectors;

import lib.db.Retry;
import lib.db.Status;

public class ClassifyHandler implements StateHandler {
    @Override
    public void handle(Retry retry) {
        System.out.printf("Handling format for retry: id=%d, uid=%d, data=%s\n", retry.getId(), retry.getUid(), retry.getData());
        String frequency = Arrays.stream(retry.getData().split(","))
                .collect(Collectors.groupingBy(
                        n -> n,                // key = number itself
                        Collectors.counting()   // value = frequency
                ))
                .entrySet()
                .stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .sorted() // optional: sort by number
                .collect(Collectors.joining(","));
        retry.setData(frequency);
        retry.setStatus(Status.SUCCESS);
    }
}