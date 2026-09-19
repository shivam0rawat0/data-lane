package lib.executor.stage;

import lib.db.Context;

public interface Handler {
    public void handle(Context retry);
}