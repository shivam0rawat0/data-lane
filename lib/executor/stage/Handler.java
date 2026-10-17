package lib.executor.stage;

import lib.db.Context;
import lib.util.Cache;

public interface Handler {
    public static Cache cache = new Cache();
    public void handle(Context retry);
}