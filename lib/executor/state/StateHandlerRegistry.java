package lib.executor.state;

import lib.db.Retry;

public class StateHandlerRegistry {
    public static StateHandler getHandler(Retry retry) {
        switch (retry.getStatus()) {
             case PENDING:
                return new FetchHandler();
            case FETCH:
            case FETCH_RETRY:
                return new FetchHandler();
            case FORMAT:
            case FORMAT_RETRY:
                return new FormatHandler();
            case CLASSIFY:
            case CLASSIFY_RETRY:
                return new ClassifyHandler();
            case SUCCESS:
            case FAILED:
                return null;
            default:
                throw new IllegalArgumentException("Unknown state: " + retry.getStatus());
        }
    }
}
