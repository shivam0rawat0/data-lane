package lib.executor.state;

import java.util.Arrays;

import lib.db.Context;
import lib.db.Status;
import lib.executor.Handler;

public class FetchHandler implements Handler {
    @Override
    public void handle(Context retry) {
        System.out.printf("Split stage for retry: id=%d, uid=%d, data=%s\n", retry.getId(), retry.getUid(), retry.getData());
        String sorted = Arrays.stream(retry.getData().split(","))
            .map(x -> Integer.parseInt(x))
            .sorted()
            .map(x -> String.valueOf(x))
            .reduce((a, b) -> String.valueOf(a) + "," + String.valueOf(b))
            .orElse("");
        retry.setData(sorted);
        retry.setStatus(Status.SUCCESS);
    }
}