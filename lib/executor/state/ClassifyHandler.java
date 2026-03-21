package lib.executor.state;

import lib.db.Retry;
import lib.db.Status;

public class ClassifyHandler implements StateHandler {
    @Override
    public Status handle(Retry retry) {
        System.out.printf("Handling format for retry: id=%d, uid=%d\n", retry.getId(), retry.getUid());
        return Status.SUCCESS;
    }
}