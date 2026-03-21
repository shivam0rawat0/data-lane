package lib.executor.state;

import lib.db.Retry;
import lib.db.Status;

public interface StateHandler {
    Status handle(Retry retry) throws Exception;

    public static Status getNextState(Retry retry) {
        switch (retry.getStatus()) {
            case PENDING:
                return Status.FETCH;
            case FETCH:
                return Status.FORMAT;
            case FORMAT:
                return Status.CLASSIFY;
            case CLASSIFY:
                return Status.SUCCESS;
            default:
                throw new IllegalArgumentException("Unknown state: " + retry.getStatus());
        }
    }
}